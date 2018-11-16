package com.adrosonic.adroscanner.util

class RegexMatcher{

    companion object {

        fun isPhoneNumber(phone: String): Boolean{

            val phoneText = phone.toLowerCase()
                    .replace(".","")
                    .replace(":","")
                    .replace(",","")
                    .replace("mob","")
                    .replace("mobile","")
                    .replace("tel","")
                    .replace("phone","")
                    .replace("cell","")
                    .replace("c","")
                    .replace("t","")
                    .replace("f","")

            phoneText.replace("o","0")
                    .replace("t","1")

            return phoneText.matches(Regex("(\\+?)(\\([0-9OoA]{3}\\))?([\\s-0-9OoA]+)\$")) && phoneText.length > 6
        }

        fun isEmailId(email: String): Boolean{
            return email.matches(Regex("([\\sa-zA-Z0-9_.]+)@([a-zA-Z0-9_.]+)\\.([a-zA-Z]{2,5})$"))
        }

        fun isNameandDesignation(text: String): Boolean{
            return text.isNotEmpty() && !text.contains("@") && !text.any { it.isDigit() && !text.contains(".co") && !text.contains("w.")}
        }

        fun isPinCode(pin: String): Boolean{
            val pinText = pin.filter { it.isDigit() }
            return pinText.isNotEmpty() && pinText.length < 7
        }

    }

}