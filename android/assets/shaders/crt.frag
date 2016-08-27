#define RGBA(r, g, b, a)	vec4(float(r)/255.0, float(g)/255.0, float(b)/255.0, float(a)/255.0)

//const vec3 kBackgroundColor = RGBA(0xff, 0x00, 0xff, 0xff).rgb; // test magenta

// Emulated input resolution.
	// Fix resolution to set amount.
	// Note: 256x224 is the most common resolution of the SNES, and that of Super Mario World.
	vec2 res = vec2(
		256.0 / 1.0,
		224.0 / 1.0
	);


// Hardness of scanline.
//	-8.0 = soft
// -16.0 = medium
float sHardScan = -4.0;

// Hardness of pixels in scanline.
// -2.0 = soft
// -4.0 = hard
const float kHardPix = -3.0;

// Display warp.
// 0.0 = none
// 1.0 / 8.0 = extreme
const vec2 kWarp = vec2(1.0 / 32.0, 1.0 / 24.0);
//const vec2 kWarp = vec2(0);

// Amount of shadow mask.
float kMaskDark = 0.5;
float kMaskLight = 1.5;


// Distortion of scanlines, and end of screen alpha.
vec2 warp(vec2 pos)
{
	pos = pos * 2.0 - 1.0;
	pos *= vec2(
		1.0 + (pos.y * pos.y) * kWarp.x,
		1.0 + (pos.x * pos.x) * kWarp.y
	);
	return pos * 0.5 + 0.5;
}

void main() {

}
