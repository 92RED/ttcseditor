package io.github.ar4093.ttcsedit;

public class HFloat {
	float value;
	float eps = 0.0003f;
	
	
	public HFloat ( float f ) {
		value = f;
		value = new HFloat(upperByte(), lowerByte()).floatValue();
	}
	
	public HFloat ( HFloat hf ) {
		this(hf.floatValue());
	}
	
	public HFloat ( int i ) {
		int hbias = 15;
		int sbias = 127;
		int sign = i >> 15 & 1;
		int exponent = ((i >> 10) & 0x1F) - hbias;
		int mantissa = i & 0x3FF;
		int nval = (sign << 31) | ((exponent + sbias) << 23) | (mantissa << 13);
		value = Float.intBitsToFloat(nval);
	}
	
	public HFloat ( byte upper, byte lower ) {
		this(((upper < 0 ? 256 + upper : upper) << 8) | (lower < 0 ? 256 + lower : lower));
		if(ColorSetEditor.DEBUG2) {
			try {ColorSetEditor.logwriter.write(String.format("Instanced new Hfloat: %02X %02X -> %s\n", upper, lower, this.toByteString())); ColorSetEditor.logwriter.flush();} catch(Exception e){e.printStackTrace();}
		}
	}
	
	public String toString () {
		return "HFloat: Value: " + value + "; Bytes: " + toByteString() + "; Corr: " + intValue();
	}
	
	public float floatValue () {
		return value;
	}
	
	public int intValue () {
		if(value < eps)
			return 0;
		return (int) Math.ceil(value * 255f);
	}
	
	public String toByteString () {
		return String.format("%04X", toBits() & 0xFFFF);
	}
	
	public int toBits () {
		if (value == 0.0f)
			return 0;
		int hbias = 15;
		int sbias = 127;
		int raw = Float.floatToIntBits(value);
		int sign = value<0?1:0;
		int exponent = ((raw >> 23) & 0xFF) - sbias;
		int mantissa = raw & 0x7FFFFF;
		return (sign << 15) | ((exponent + hbias) << 10) | (mantissa >> 13);
	}
	
	public byte[] bytesValue () {
		byte[] ret = {upperByte(), lowerByte()};
		return ret;
	}
	
	public byte lowerByte () {
		return (byte) (toBits() & 0xFF);
	}
	
	public byte upperByte () {
		return (byte) ((toBits() >> 8) & 0xFF);
	}
}
