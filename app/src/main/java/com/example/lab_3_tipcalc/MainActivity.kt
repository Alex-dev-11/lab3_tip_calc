package com.example.lab_3_tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =============================================================================
// ГЛАВНЫЙ КЛАСС ACTIVITY - ТОЧКА ВХОДА ПРИЛОЖЕНИЯ
// =============================================================================

/**
 * MainActivity - основной класс приложения, наследуется от ComponentActivity
 * Это точка входа в приложение, аналогично main() в C++
 */
class MainActivity : ComponentActivity() {
    /**
     * onCreate() - вызывается при создании Activity
     * @param savedInstanceState - сохраненное состояние (если приложение было пересоздано)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent { } - устанавливает Compose UI как содержимое экрана
        setContent {
            DiscountCalculatorApp() // Запускаем наше приложение
        }
    }
}

// =============================================================================
// КОРНЕВОЙ COMPOSABLE - НАСТРОЙКА ТЕМЫ И ОСНОВНОГО ЭКРАНА
// =============================================================================

/**
 * DiscountCalculatorApp - корневая Composable функция приложения
 * Отвечает за применение темы и отображение основного экрана
 * @Composable - пометка что это функция UI (аналогично компоненту в React)
 */
@Composable
fun DiscountCalculatorApp() {
    // MaterialTheme - применяет Material Design стили ко всему приложению
    MaterialTheme {
        // Surface - контейнер с фоном и тенями (основная поверхность)
        Surface(
            modifier = Modifier.fillMaxSize(), // fillMaxSize() - занимает весь доступный размер
            color = MaterialTheme.colorScheme.background // Цвет фона из темы
        ) {
            DiscountCalculatorScreen() // Основной экран калькулятора
        }
    }
}
