package com.example.lumi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lumi.ui.theme.LumiTheme

@Composable
fun LumiProfileScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

    }
}

@Preview
@Composable
fun LumiProfileScreenPreview() {
    LumiTheme {
        LumiProfileScreen()
    }
}
