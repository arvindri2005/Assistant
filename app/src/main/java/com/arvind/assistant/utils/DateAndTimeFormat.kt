package com.arvind.assistant.utils

import java.time.format.DateTimeFormatter



val timeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern("hh:mm a")