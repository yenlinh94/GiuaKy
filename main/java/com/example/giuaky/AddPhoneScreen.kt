package com.example.giuaky

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.giuaky.until.base64ToBitmap
import com.example.giuaky.until.uriToBase64

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhoneScreen(
    vm: PhoneViewModel,
    onDone: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageBase64 by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imageBase64 = uriToBase64(context, uri)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Thêm sản phẩm") }) }) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Tên sản phẩm") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(category, { category = it }, label = { Text("Loại sản phẩm") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(price, { price = it }, label = { Text("Giá") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = { picker.launch("image/*") }) { Text("Chọn hình ảnh") }

            imageBase64?.let { b64 ->
                base64ToBitmap(b64)?.let { bmp ->
                    Image(bitmap = bmp.asImageBitmap(), contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp))
                }
            }

            Button(
                onClick = {
                    vm.addPhone(name.trim(), category.trim(), price.trim(), imageBase64)
                    onDone()
                },
                enabled = name.isNotBlank() && category.isNotBlank() && price.isNotBlank()
            ) { Text("Lưu") }
        }
    }
}
