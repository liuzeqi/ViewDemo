package com.liuzeqi.viewdemo

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.liuzeqi.viewdemo.databinding.ActivityPhoneInputBinding

const val TAG = "PhoneInputActivity"

/**
 * 手机号输入Activity
 * editText: 输入内容自动添加空格： 155 0000 0000
 * @author zeqi.liu
 */
class PhoneInputActivity : AppCompatActivity(),
    TextWatcher {

    private lateinit var viewBinding: ActivityPhoneInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPhoneInputBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.editText.let {
            it.addTextChangedListener(this)
            it.filters = arrayOf(InputFilter.LengthFilter(13))
        }

        viewBinding.btnClear.setOnClickListener {
            viewBinding.editText.setText("")
            viewBinding.editText.setSelection(0)
        }
    }

    /**
     * 输入框内容将要发生变化
     * s: 变化前的内容
     * 从start开始，长度为count内容将要被替换为长度为after的内容
     *
     * 输入：count为0，新输入内容长度为after
     * 删除：count为删除的长度，after为0
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d(TAG, "beforeTextChanged: s = $s, start = $start, count = $count, after = $after")
    }

    /**
     * 输入框内容刚刚发生变化
     * s: 变化后的内容
     * 从start开始，长度为count内容刚刚发生变化，变化前的长度为before
     *
     * 输入：before为0，新输入内容长度为count
     * 删除：before为删除的长度，count为0
     *
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d(TAG, "onTextChanged: s = $s, start = $start, before = $before, count = $count")

        if (s == null || s.isEmpty()) return

        // 去除空格
        val content = s.toString().replace(" ", "")
        var newStart = start
        if (start > 8) {
            newStart -= 2
        } else if (start > 3) {
            newStart -= 1
        }

        // 光标位置
        var cursorPosition = if (before == 0) {
            newStart + count
        } else {
            newStart
        }

        // 添加空格
        val sb = StringBuilder()
        for (i in content.indices) {
            if ((i == 3 || i == 7)) {
                // index为3或7时候添加空格
                sb.append(" ")
                if (cursorPosition > i) {
                    cursorPosition++
                }
            }
            sb.append(content[i])
        }

        // 删除空格后的字符则一起删除空格
        // 若光标后还有字符，则光标位置多往前移动一位；
        // 若光标后没有字符，则多删除一位
        if (count == 0 && before == 1) {
            if (cursorPosition == 4 || cursorPosition == 9) {
                cursorPosition--

                if (sb.endsWith(" ")) {
                    sb.delete(sb.length - 1, sb.length)
                }

            }
        }

        viewBinding.editText.let {
            it.removeTextChangedListener(this)
            it.setText(sb.toString())
            it.setSelection(cursorPosition)
            it.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }
}