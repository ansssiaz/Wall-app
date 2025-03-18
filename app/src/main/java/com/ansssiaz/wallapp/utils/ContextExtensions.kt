package com.ansssiaz.wallapp.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Создаёт и показывает Toast
 * @param res – ссылка на строковый ресурс, который показываем
 * @param short - если true, то Toast.LENGTH_SHORT, иначе Toast.LENGTH_LONG
 */
fun Context.toast(@StringRes res: Int, short: Boolean = true) = if (short) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
} else {
    Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}