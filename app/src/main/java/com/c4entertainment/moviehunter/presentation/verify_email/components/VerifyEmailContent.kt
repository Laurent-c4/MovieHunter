package com.c4entertainment.moviehunter.presentation.verify_email.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c4entertainment.moviehunter.util.Constants.ALREADY_VERIFIED
import com.c4entertainment.moviehunter.util.Constants.SPAM_EMAIL


@Composable
fun VerifyEmailContent(
    padding: PaddingValues,
    reloadUser: () -> Unit,
    resendVerificationEmail: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(start = 32.dp, end = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                reloadUser()
            },
            text = ALREADY_VERIFIED,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = SPAM_EMAIL,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = resendVerificationEmail) {
            Text(text = "Resend verification email")
        }
    }
}