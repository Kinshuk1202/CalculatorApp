package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun pmAction(view: View) {
        var s = Workingtv.text
        var r = ""
        if (s.first() != '-') {
            r = "-"
            r += s;
            Workingtv.text = r
        }
        else
        {
            val x = Workingtv.length();
            Workingtv.text =  Workingtv.text.subSequence(1,x)
        }
    }


    fun numberAction(view: View){
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    Workingtv.append(view.text)

                canAddDecimal = false
            }
            else{
                Workingtv.append(view.text)
                canAddDecimal = true
            }

            canAddOperation  = true
        }
    }
    fun operationAction(view: View){
        if(view is Button && canAddOperation )
        {
            Workingtv.append(view.text)
            canAddOperation  = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View){
        Workingtv.text = ""
        Resulttv.text = ""
    }
    fun allBAckAction(view: View){
        val length = Workingtv.length()
        if(length>0)
            Workingtv.text = Workingtv.text.subSequence(0,length-1)
    }
    fun EqulasAction(view: View){
        Resulttv.text = cacl()
    }

    private fun cacl(): String {
        val digitope = digitsOperators()
        if(digitope.isEmpty())
            return ""
        val time = timedivision(digitope)
        if(time.isEmpty())
                return ""
        val result = addSub(time)
        return result.toString()

    }
    private fun addSub(plist:MutableList<Any>):Float{
        var res = plist[0] as Float;
        for(i in plist.indices)
        {
            if(plist[i] is Char && i != plist.lastIndex)
            {
                val operator = plist[i]
                val next = plist[i+1] as Float
                if(operator == '+')
                    res += next
                if(operator == '-')
                    res -= next

            }
        }
        return res
    }
    private fun timedivision(digitope: MutableList<Any>): MutableList<Any> {
        var list = digitope
        while(list.contains('*') || list.contains('/'))
        {
            list = calcDiv(list)
        }
        return list
    }

    private fun calcDiv(list: MutableList<Any>): MutableList<Any> {
        val newlist = mutableListOf<Any>()
        var reindex = list.size
        for(i in list.indices)
        {
            if(list[i] is Char && i!=list.lastIndex && i<reindex)
            {
                val operator = list[i]
                val pre = list[i-1] as Float
                val nxt = list[i+1] as Float
                when(operator)
                {
                    '*'->
                    {
                        newlist.add(pre*nxt)
                        reindex = i+1
                    }
                    '/'->
                    {
                        newlist.add(pre/nxt)
                        reindex  = i+1
                    }
                    else->
                    {
                        newlist.add(pre)
                        newlist.add(operator)
                    }
                }
            }
            if(i>reindex)
                newlist.add(list[i])
        }
        return  newlist
    }

    private fun digitsOperators():MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currDigit = ""
        for(c in Workingtv.text)
        {
            if(c.isDigit()||c == '.')
                currDigit += c;
            else
            {
                list.add(currDigit.toFloat())
                currDigit = ""
                list.add(c)
            }
        }
        if(currDigit!="")
                list.add(currDigit.toFloat())

        return list
    }
}
