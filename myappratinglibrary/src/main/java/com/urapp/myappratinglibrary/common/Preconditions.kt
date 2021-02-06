package com.urapp.myappratinglibrary.common


object Preconditions {

    fun checkArgument(expression: Boolean) {
        if (!expression) {
            throw IllegalArgumentException()
        }
    }


    fun checkArgument(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalArgumentException(errorMessage.toString())
        }
    }


    fun checkArgument(expression: Boolean,
                      errorMessageTemplate: String, vararg errorMessageArgs: Any) {
        if (!expression) {
            throw IllegalArgumentException(
                    format(errorMessageTemplate, *errorMessageArgs))
        }
    }

    fun checkState(expression: Boolean) {
        if (!expression) {
            throw IllegalStateException()
        }
    }


    fun checkState(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalStateException(errorMessage.toString())
        }
    }


    fun checkState(expression: Boolean,
                   errorMessageTemplate: String, vararg errorMessageArgs: Any) {
        if (!expression) {
            throw IllegalStateException(
                    format(errorMessageTemplate, *errorMessageArgs))
        }
    }


    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }


    fun <T> checkNotNull(reference: T?, errorMessage: Any): T {
        if (reference == null) {
            throw NullPointerException(errorMessage.toString())
        }
        return reference
    }


    fun <T> checkNotNull(reference: T?, errorMessageTemplate: String,
                         vararg errorMessageArgs: Any): T {
        if (reference == null) {
            // If either of these parameters is null, the right thing happens anyway
            throw NullPointerException(
                    format(errorMessageTemplate, *errorMessageArgs))
        }
        return reference
    }


    @JvmOverloads
    fun checkElementIndex(index: Int, size: Int, desc: String = "index"): Int {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException(badElementIndex(index, size, desc))
        }
        return index
    }

    private fun badElementIndex(index: Int, size: Int, desc: String): String {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index)
        } else if (size < 0) {
            throw IllegalArgumentException("negative size: " + size)
        } else { // index >= size
            return format("%s (%s) must be less than size (%s)", desc, index, size)
        }
    }


    @JvmOverloads
    fun checkPositionIndex(index: Int, size: Int, desc: String = "index"): Int {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw IndexOutOfBoundsException(badPositionIndex(index, size, desc))
        }
        return index
    }

    private fun badPositionIndex(index: Int, size: Int, desc: String): String {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index)
        } else if (size < 0) {
            throw IllegalArgumentException("negative size: " + size)
        } else { // index > size
            return format("%s (%s) must not be greater than size (%s)",
                    desc, index, size)
        }
    }


    fun checkPositionIndexes(start: Int, end: Int, size: Int) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (start < 0 || end < start || end > size) {
            throw IndexOutOfBoundsException(badPositionIndexes(start, end, size))
        }
    }

    private fun badPositionIndexes(start: Int, end: Int, size: Int): String {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index")
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index")
        }
        // end < start
        return format("end index (%s) must not be less than start index (%s)",
                end, start)
    }


    internal fun format(template: String, vararg args: Any): String {
        // start substituting the arguments into the '%s' placeholders
        val builder = StringBuilder(
                template.length + 16 * args.size)
        var templateStart = 0
        var i = 0
        while (i < args.size) {
            val placeholderStart = template.indexOf("%s", templateStart)
            if (placeholderStart == -1) {
                break
            }
            builder.append(template.substring(templateStart, placeholderStart))
            builder.append(args[i++])
            templateStart = placeholderStart + 2
        }
        builder.append(template.substring(templateStart))

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.size) {
            builder.append(" [")
            builder.append(args[i++])
            while (i < args.size) {
                builder.append(", ")
                builder.append(args[i++])
            }
            builder.append("]")
        }

        return builder.toString()
    }
}
