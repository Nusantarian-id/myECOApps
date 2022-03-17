package id.myeco.myeco.utils

import android.content.Context
import android.widget.Toast

fun Context.toastShort(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.toastLong(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
// remove double quote
fun String.removeQuotation(): String{
    if (startsWith("\"") && endsWith("\"")) {
        return drop(1).dropLast(1)
    }
    return this
}