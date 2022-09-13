package cn.kt.android.util


val Float.dp get() = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_DIP,
    this,
    android.content.res.Resources.getSystem().displayMetrics
)

fun android.app.Activity.toast(message: CharSequence, duration: Int = android.widget.Toast.LENGTH_SHORT){
    android.widget.Toast.makeText(this, message, duration).show()
}
