package ir.mahan.wikifoodia.utils.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import ir.mahan.wikifoodia.ui.register.BodyRegister

@Module
@InstallIn(FragmentComponent::class)
object BodyModule {
    @Provides
    fun provideBodyRegister(): BodyRegister = BodyRegister()

}