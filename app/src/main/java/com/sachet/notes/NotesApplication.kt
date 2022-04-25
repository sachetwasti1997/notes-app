package com.sachet.notes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is the application level class, will govern the entire application, and give hilt the
 * capability of binding all the dependencies throughout the entire application
 */
@HiltAndroidApp
class NotesApplication: Application() {
}