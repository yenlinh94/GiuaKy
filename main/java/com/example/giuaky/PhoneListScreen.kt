package com.example.giuaky

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.giuaky.data.PhoneItem
import com.example.giuaky.until.base64ToBitmap
import com.example.giuaky.until.uriToBase64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneListScreen(
    vm: PhoneViewModel,
    onAdd: () -> Unit
) {
    val phones by vm.phones.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) { vm.loadPhones() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách sản phẩm") },
                actions = {
                    TextButton(onClick = onAdd) { Text("Thêm") }
                    TextButton(onClick = { vm.logout() }) { Text("Đăng xuất") }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner)) {
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
            error?.let {
                Text(
                    "Lỗi: $it",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(phones, key = { it.id }) { phone ->
                    PhoneRow(phone, vm)
                }
            }
        }
    }
}

@Composable
private fun PhoneRow(phone: PhoneItem, vm: PhoneViewModel) {
    var openEdit by remember { mutableStateOf(false) }
    var openDelete by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp) // Tạo bóng nhẹ
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bmp = phone.image?.let { base64ToBitmap(it) }
            if (bmp != null) {
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            } else {
                Box(Modifier.size(56.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(
                    phone.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Loại: ${phone.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Giá: ${phone.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            TextButton(onClick = { openEdit = true }) { Text("Sửa") }
            TextButton(onClick = { openDelete = true }) { Text("Xóa") }
        }
    }

    if (openEdit) EditPhoneDialog(phone, vm, onDismiss = { openEdit = false })
    if (openDelete) ConfirmDeleteDialog(
        onConfirm = { vm.deletePhone(phone.id); openDelete = false },
        onDismiss = { openDelete = false }
    )
}

@Composable
private fun EditPhoneDialog(phone: PhoneItem, vm: PhoneViewModel, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue(phone.name)) }
    var category by remember { mutableStateOf(TextFieldValue(phone.category)) }
    var price by remember { mutableStateOf(TextFieldValue(phone.price)) }
    var newImageB64 by remember { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) newImageB64 = uriToBase64(context, uri)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sửa sản phẩm") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên sản phẩm") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Loại sản phẩm") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá") })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { picker.launch("image/*") }) { Text("Đổi hình") }
                    if (newImageB64 != null) Text("Đã chọn hình mới")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                vm.updatePhone(
                    docId = phone.id,
                    name = name.text.takeIf { it.isNotBlank() && it != phone.name },
                    category = category.text.takeIf { it.isNotBlank() && it != phone.category },
                    price = price.text.takeIf { it.isNotBlank() && it != phone.price },
                    image = newImageB64
                )
                onDismiss()
            }) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
private fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xóa sản phẩm") },
        text = { Text("Bạn có chắc muốn xóa sản phẩm này?") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Xóa") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}
