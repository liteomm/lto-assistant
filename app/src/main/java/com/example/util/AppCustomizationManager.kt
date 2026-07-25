package com.example.util

import androidx.compose.ui.graphics.Color

data class BackgroundOption(
    val id: String,
    val name: String,
    val icon: String,
    val description: String
)

data class CardStyleOption(
    val id: String,
    val name: String,
    val icon: String,
    val description: String
)

data class AccentColorOption(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val isGradient: Boolean = false
) {
    val primary: Color get() = primaryColor
    val secondary: Color get() = secondaryColor
}

data class ChatBubbleStyleOption(
    val id: String,
    val name: String,
    val icon: String
)

data class FontOption(
    val id: String,
    val name: String,
    val fontType: String,
    val sampleText: String
)

data class AppIconOption(
    val id: String,
    val name: String,
    val emoji: String,
    val bgColors: List<Color>
)

data class HomeLayoutOption(
    val id: String,
    val name: String,
    val icon: String,
    val description: String
)

object AppCustomizationManager {

    val defaultBackground = "aurora_lights"
    val defaultCardStyle = "glass"
    val defaultAccent = "cyan"
    val defaultBubbleStyle = "modern"
    val defaultFont = "default_sans"
    val defaultAppIcon = "cyber_lto"
    val defaultHomeLayout = "classic"

    val backgroundOptions = listOf(
        BackgroundOption("aurora_lights", "Aurora Lights", "", "Glowing polar aurora ribbons shifting across dusk canvas"),
        BackgroundOption("blue_nebula", "Blue Nebula", "", "Deep orbital cyan and sapphire cosmic gas clouds"),
        BackgroundOption("purple_galaxy", "Purple Galaxy", "", "Deep space dark violet galaxy with pulsing stardust"),
        BackgroundOption("cyber_grid", "Cyber Grid", "", "3D futuristic perspective cyan gridlines moving in space"),
        BackgroundOption("floating_particles", "Floating Particles", "", "Upward illuminated glowing particle dust"),
        BackgroundOption("glass_waves", "Glass Waves", "", "Frosted translucent liquid wave curves"),
        BackgroundOption("liquid_gradient", "Liquid Gradient", "", "Flowing organic fluid metaballs shifting colors"),
        BackgroundOption("neon_rings", "Neon Rings", "", "Concentric pulsating neon energy rings"),
        BackgroundOption("matrix_rain", "Matrix Rain", "", "Digital code waterfall with glowing stream trails"),
        BackgroundOption("space_stars", "Space Stars", "", "Twinkling starfield with deep cosmos warp drift"),
        BackgroundOption("northern_lights", "Northern Lights", "", "Polar green and emerald light curtains"),
        BackgroundOption("fireflies", "Fireflies", "", "Soft drifting amber and gold firefly sparks"),
        BackgroundOption("ocean_glow", "Ocean Glow", "", "Deep underwater caustics and shimmering light rays"),
        BackgroundOption("crystal_blur", "Crystal Blur", "", "Floating prismatic crystal polygons"),
        BackgroundOption("geometric_motion", "Geometric Motion", "", "Rotating wireframe geometric shapes"),
        BackgroundOption("abstract_mesh", "Abstract Mesh", "", "Interconnected drift node network"),
        BackgroundOption("neural_network", "AI Neural Network", "", "Pulsing neural nodes with synaptic signal flashes"),
        BackgroundOption("floating_bubbles", "Floating Bubbles", "", "Iridescent rising glass spheres with light reflections"),
        BackgroundOption("soft_smoke", "Soft Smoke", "", "Volumetric rolling ethereal smoke clouds"),
        BackgroundOption("dynamic_gradient", "Dynamic Gradient", "", "Continuous multi-color gradient cycle")
    )

    val cardStyles = listOf(
        CardStyleOption("glass", "Glass Card", "", "Frosted glassmorphism with subtle white glow border"),
        CardStyleOption("neon", "Neon Card", "", "High-contrast electric outline with ambient glow"),
        CardStyleOption("gradient", "Gradient Card", "", "Linear multi-color surface overlay"),
        CardStyleOption("material3", "Material 3", "", "Clean tonal surface with standard rounded corners"),
        CardStyleOption("liquid_glass", "Liquid Glass", "", "Fluid specular reflection edge with soft gradient"),
        CardStyleOption("floating", "Floating Card", "", "Deep drop shadow with clean floating canvas"),
        CardStyleOption("soft_shadow", "Soft Shadow", "", "Smooth ambient shadow without heavy borders"),
        CardStyleOption("minimal", "Minimal Card", "", "Ultra-clean flat surface with hairline border"),
        CardStyleOption("premium_dark", "Premium Dark", "", "Pitch black OLED surface with gold/cyan accent edges"),
        CardStyleOption("colorful_study", "Colorful Study", "", "Vibrant dual-color study theme background"),
        CardStyleOption("cyber", "Cyber Card", "", "Tech corner cuts with glowing cyber outline"),
        CardStyleOption("aurora", "Aurora Card", "", "Shimmering polar lights gradient background")
    )

    val accentColors = listOf(
        AccentColorOption("cyan", "Cyber Cyan", Color(0xFF00F2FE), Color(0xFF4FACFE)),
        AccentColorOption("blue", "Electric Blue", Color(0xFF00A8FF), Color(0xFF0066FF)),
        AccentColorOption("purple", "Ultra Violet", Color(0xFFA855F7), Color(0xFF7C3AED)),
        AccentColorOption("green", "Emerald Pulse", Color(0xFF10B981), Color(0xFF059669)),
        AccentColorOption("orange", "Neon Sunset", Color(0xFFF97316), Color(0xFFEA580C)),
        AccentColorOption("pink", "Hot Magenta", Color(0xFFEC4899), Color(0xFFDB2777)),
        AccentColorOption("red", "Crimson Flame", Color(0xFFEF4444), Color(0xFFDC2626)),
        AccentColorOption("gold", "Imperial Gold", Color(0xFFEAB308), Color(0xFFCA8A04)),
        AccentColorOption("white", "Crisp Minimal", Color(0xFFF8FAFC), Color(0xFFE2E8F0)),
        AccentColorOption("gradient", "Dynamic Gradient", Color(0xFF00F2FE), Color(0xFFA855F7), isGradient = true)
    )

    val chatBubbleStyles = listOf(
        ChatBubbleStyleOption("rounded", "Rounded", ""),
        ChatBubbleStyleOption("modern", "Modern Pill", ""),
        ChatBubbleStyleOption("minimal", "Minimal Flat", ""),
        ChatBubbleStyleOption("imessage", "iMessage Style", ""),
        ChatBubbleStyleOption("material", "Material 3", ""),
        ChatBubbleStyleOption("glass", "Frosted Glass", ""),
        ChatBubbleStyleOption("gradient", "Dual Gradient", ""),
        ChatBubbleStyleOption("cyber", "Cyber Tech", ""),
        ChatBubbleStyleOption("neon", "Neon Glow", ""),
        ChatBubbleStyleOption("capsule", "Soft Capsule", "")
    )

    val fontOptions = listOf(
        FontOption("default_sans", "Default Sans", "sans-serif", "LTO Assistant • Your Smart AI Companion"),
        FontOption("modern_clean", "Roboto Clean", "sans-serif-medium", "LTO Assistant • quick revision & exams"),
        FontOption("tech_minimal", "Inter Minimal", "sans-serif-light", "LTO Assistant • ଲାଇଟ୍ ଓଏମ୍ଏମ୍ ଏଆଇ"),
        FontOption("cyber_display", "Space Grotesk", "sans-serif-condensed", "LTO Assistant • 20+ Animated Backgrounds"),
        FontOption("elegant", "Playfair Serif", "serif", "LTO Assistant • High Performance Neural Engine"),
        FontOption("rounded_friendly", "Poppins Rounded", "sans-serif-rounded", "LTO Assistant • Smart AI Study Buddy"),
        FontOption("developer_mono", "Fira Mono Code", "monospace", "val ai = LTOAssistant.getInstance()"),
        FontOption("futuristic_bold", "Outfit Bold", "sans-serif-black", "LTO ASSISTANT PRO SYSTEM"),
        FontOption("multilingual_universal", "Noto Universal (EN/HI/OR)", "sans-serif", "LTO Assistant • LTO ସହାୟକ • एलटीओ सहायक"),
        FontOption("classic_editorial", "Cinzel Editorial", "serif", "LTO Assistant • Master Your Academic Journey")
    )

    val appIcons = listOf(
        AppIconOption("cyber_lto", "Cyber LTO Core", "", listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))),
        AppIconOption("neon_study", "Neon Book", "", listOf(Color(0xFFA855F7), Color(0xFFEC4899))),
        AppIconOption("aurora_brain", "Aurora Brain", "", listOf(Color(0xFF10B981), Color(0xFF00F2FE))),
        AppIconOption("minimal_glass", "Minimal Glass", "", listOf(Color(0xFF334155), Color(0xFF0F172A))),
        AppIconOption("golden_crest", "Imperial Gold", "", listOf(Color(0xFFEAB308), Color(0xFFF97316))),
        AppIconOption("quantum_orb", "Quantum Orb", "", listOf(Color(0xFF6366F1), Color(0xFFA855F7))),
        AppIconOption("hologram_spark", "Hologram Spark", "", listOf(Color(0xFF38BDF8), Color(0xFF818CF8))),
        AppIconOption("midnight_wave", "Midnight Wave", "", listOf(Color(0xFF0284C7), Color(0xFF0F172A))),
        AppIconOption("emerald_pulse", "Bio Emerald", "", listOf(Color(0xFF059669), Color(0xFF10B981))),
        AppIconOption("futuristic_core", "Hyper Core", "", listOf(Color(0xFFEF4444), Color(0xFFF97316)))
    )

    val homeLayouts = listOf(
        HomeLayoutOption("classic", "Classic Layout", "", "Balanced top bar, quick status pill, 2x2 suggestion cards"),
        HomeLayoutOption("minimal", "Minimal Focus", "", "Streamlined interface with quick horizontal prompt chips"),
        HomeLayoutOption("premium", "Premium Studio", "", "Large glass hero card with stats & 3x2 feature dashboard"),
        HomeLayoutOption("modern", "Modern Feed", "", "Category filter bar with full-width interactive cards"),
        HomeLayoutOption("compact", "Compact Density", "", "High density layout maximized for large reading area")
    )

    val animationStyles = listOf("Fade", "Slide", "Zoom", "Scale", "Ripple")
    val animationSpeeds = listOf("Slow", "Normal", "Fast")
    val animationIntensities = listOf("Low", "Medium", "High")
    val borderRadiuses = listOf("Small", "Medium", "Large", "Extra Large")
    val hapticStrengths = listOf("Off", "Light", "Medium", "Strong")

    val backgrounds get() = backgroundOptions
    val cardThemes get() = cardStyles
    val bubbleStyles get() = chatBubbleStyles
    val borderRadii get() = borderRadiuses

    fun getAccentByName(id: String): AccentColorOption {
        return accentColors.find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) }
            ?: accentColors.first()
    }

    fun getAccentOption(id: String): AccentColorOption = getAccentByName(id)

    fun getBackgroundById(id: String): BackgroundOption {
        return backgroundOptions.find { it.id.equals(id, ignoreCase = true) }
            ?: backgroundOptions.first()
    }
}
