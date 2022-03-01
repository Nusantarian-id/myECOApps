package id.myeco.myeco.utils

import android.content.Context
import android.widget.Toast

fun Context.toastShort(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.toastLong(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()