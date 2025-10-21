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

// =============================================================================
// ОСНОВНОЙ ЭКРАН КАЛЬКУЛЯТОРА - ВСЯ ЛОГИКА И ИНТЕРФЕЙС
// =============================================================================

/**
 * DiscountCalculatorScreen - основной экран приложения
 * Содержит все UI элементы и логику расчета скидок
 */
@Composable
fun DiscountCalculatorScreen() {
    // =========================================================================
    // ОБЪЯВЛЕНИЕ СОСТОЯНИЯ ПРИЛОЖЕНИЯ
    // =========================================================================

    // dishCount - хранит количество блюд (текст из поля ввода)
    // remember { mutableStateOf() } - сохраняет значение при перерисовке UI
    // by - синтаксический сахар для удобной работы с состоянием
    var dishCount by remember { mutableStateOf("") }

    // totalAmount - хранит общую сумму заказа
    var totalAmount by remember { mutableStateOf("") }

    // sliderValue - хранит значение слайдера дополнительной скидки (0-25%)
    var sliderValue by remember { mutableStateOf(0f) }

    // =========================================================================
    // РАСЧЕТ СКИДОК И ИТОГОВОЙ СУММЫ
    // =========================================================================

    // Рассчитываем скидку на основе количества блюд (автоматически)
    // calculateDiscount() определяет процент скидки по количеству блюд
    val discountPercentage = calculateDiscount(dishCount.toIntOrNull() ?: 0)

    // Рассчитываем итоговую сумму с учетом всех скидок
    // calculateTotalAmount() вычисляет финальную сумму после применения скидок
    val calculatedAmount = calculateTotalAmount(
        totalAmount = totalAmount.toDoubleOrNull() ?: 0.0, // Безопасное преобразование в число
        discountPercentage = discountPercentage,           // Автоматическая скидка по блюдам
        additionalDiscount = sliderValue.toInt()           // Дополнительная скидка со слайдера
    )

    // =========================================================================
    // ПОСТРОЕНИЕ ПОЛЬЗОВАТЕЛЬСКОГО ИНТЕРФЕЙСА
    // =========================================================================

    // Column - вертикальное расположение всех элементов интерфейса
    Column(
        modifier = Modifier
            .fillMaxSize()          // Занимает весь экран
            .padding(24.dp),        // Отступы от краев экрана
        horizontalAlignment = Alignment.CenterHorizontally // Выравнивание по центру
    ) {
        // =====================================================================
        // ЗАГОЛОВОК ПРИЛОЖЕНИЯ
        // =====================================================================

        Text(
            text = "Расчёт суммы заказа",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // =====================================================================
        // ПОЛЕ ВВОДА КОЛИЧЕСТВА БЛЮД
        // =====================================================================

        // Column для группировки метки и поля ввода
        Column(
            modifier = Modifier
                .fillMaxWidth()              // Занимает всю ширину
                .padding(bottom = 24.dp)     // Отступ снизу
        ) {
            // Метка "Количество блюд"
            Text(
                text = "Количество блюд:",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Поле ввода с рамкой
            OutlinedTextField(
                value = dishCount,                           // Текущее значение
                onValueChange = { dishCount = it },          // Обновление состояния при вводе
                modifier = Modifier.fillMaxWidth(),          // На всю ширину
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Цифровая клавиатура
                singleLine = true                            // Однострочное поле
            )
        }

        // =====================================================================
        // ПОЛЕ ВВОДА ОБЩЕЙ СУММЫ
        // =====================================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Общая сумма:",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = totalAmount,
                onValueChange = { totalAmount = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Клавиатура с цифрами и точкой
                singleLine = true
            )
        }

        // =====================================================================
        // СЕКЦИЯ ЧАЕВЫЕ СО СЛАЙДЕРОМ ДОПОЛНИТЕЛЬНОЙ СКИДКИ
        // =====================================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Заголовок секции
            Text(
                text = "Чаевые:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Строка с значениями 0%, текущее значение, 25%
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Равномерное распределение
            ) {
                Text(text = "0%") // Минимальное значение
                Text(
                    text = "${sliderValue.toInt()}%", // Текущее значение слайдера
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "25%") // Максимальное значение
            }

            // Слайдер для выбора дополнительной скидки
            Slider(
                value = sliderValue,         // Текущее значение
                onValueChange = { sliderValue = it }, // Обновление при перемещении
                valueRange = 0f..25f,        // Диапазон от 0 до 25%
                steps = 24,                  // 25 шагов (0, 1, 2, ..., 25)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        // =====================================================================
        // СЕКЦИЯ COUPON С РАДИО-КНОПКАМИ (АВТОМАТИЧЕСКИЙ ВЫБОР)
        // =====================================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Скидка:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Группа радио-кнопок с автоматическим выбором на основе количества блюд
            DiscountRadioButtonGroup(
                selectedDiscount = discountPercentage, // Программно выбранная скидка
                modifier = Modifier.fillMaxWidth()
            )
        }

        // =====================================================================
        // КАРТОЧКА С ИТОГОВОЙ СУММОЙ
        // =====================================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Тень карточки
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Итоговая сумма:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "%.2f".format(calculatedAmount), // Форматирование до 2 знаков после запятой
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Цвет из темы
                )
            }
        }
    }
}

// =============================================================================
// КОМПОНЕНТ ГРУППЫ РАДИО-КНОПОК (ГОРИЗОНТАЛЬНОЕ РАСПОЛОЖЕНИЕ)
// =============================================================================

/**
 * DiscountRadioButtonGroup - компонент группы радио-кнопок для отображения скидок
 * Располагает кнопки горизонтально в одну строку
 * @param selectedDiscount - программно выбранная скидка (не пользователем)
 * @param modifier - модификаторы для настройки внешнего вида
 */
@Composable
fun DiscountRadioButtonGroup(
    selectedDiscount: Int,
    modifier: Modifier = Modifier
) {
    // Список доступных скидок согласно ТЗ
    val discounts = listOf(3, 5, 7, 10)

    // Горизонтальное расположение радио-кнопок в одну строку
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly, // Равномерное распределение по ширине
        verticalAlignment = Alignment.CenterVertically   // Выравнивание по центру вертикали
    ) {
        // Для каждой скидки создаем радио-кнопку
        discounts.forEach { discount ->
            // Каждая радио-кнопка в отдельном столбце с текстом
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Выравнивание по центру горизонтали
                modifier = Modifier.padding(horizontal = 4.dp)      // Отступы между кнопками
            ) {
                // Радио-кнопка с программным выбором
                RadioButton(
                    selected = discount == selectedDiscount, // Выбрана если совпадает с расчетной
                    onClick = {
                        /*
                         * НЕ ИСПОЛЬЗУЕТСЯ - выбор осуществляется программно на основе количества блюд
                         * Это соответствует требованию: "Выбор соответствующей радиокнопки
                         * реализуется программно, а не пользователем"
                         */
                    }
                )
                // Текст с процентом скидки под кнопкой
                Text(
                    text = "$discount%",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp) // Отступ от радио-кнопки
                )
            }
        }
    }
}

/