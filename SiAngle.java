package sandbox.icu.sinet;

public class SiAngle<NumericType extends Number>{
	// Variables / Enums
    public enum AngleType {
        AngleTypeRotations,
        AngleTypeDegrees,
        AngleTypeRadians,
        AngleTypeGradians,
        AngleTypeMinuteOfAngle,
        AngleTypeSecondOfArc,
        AngleTypeMilliradians,
        AngleTypeMilliradiansNATO,
        AngleTypeMilliradiansUSSR,
    }
    public static final AngleType DEFAULT_ANGLE_TYPE = AngleType.AngleTypeRadians;
    public static final Double DEFAULT_VALUE = 0.0;
    
    private NumericType value;
    private AngleType type = DEFAULT_ANGLE_TYPE;
    
    // Static Functions
    public static <NumericType extends Number> double GetFullCircleValueOfType(AngleType type) {
        // Returns Double value of one full revolution around a circle of AngleType
        switch (type) {
            case AngleTypeRotations:
                return 1.0;
            case AngleTypeDegrees:
                return 360.0;
            case AngleTypeRadians:
                return 2.0 * Math.PI;
            case AngleTypeGradians:
                return 400.0;
            case AngleTypeMinuteOfAngle:
                return 21600.0;
            case AngleTypeSecondOfArc:
                return 1296000.0;
            case AngleTypeMilliradians:
                return 6283.185307;
            case AngleTypeMilliradiansNATO:
                return 6400.0;
            case AngleTypeMilliradiansUSSR:
                return 6000.0;
            default:
                throw new IllegalArgumentException("Unknown AngleType: " + type);
        }
    }
    public static <NumericType extends Number> SiAngle<NumericType> ConvertToType(SiAngle<NumericType> angle, AngleType type) {
        if (angle.type == type) return angle;
        double newValue = (angle.toDouble() / GetFullCircleValueOfType(angle.type)) * GetFullCircleValueOfType(type);
        return new SiAngle<NumericType>(newValue, type);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Add(SiAngle<NumericType> angleA, double value) {
    	//Returns new SiAngle of the type of AngleA with the value of AngleA + value.
    	return new SiAngle<NumericType>(angleA.toDouble() + value, angleA.type);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Add(SiAngle<NumericType> angleA, SiAngle<NumericType> angleB) {
    	//Returns new SiAngle of the type of AngleA with the value of AngleA + AngleB.
    	return SiAngle.Add(angleA, angleB.ConvertToType(angleA.type).toDouble());
    }
    public static <NumericType extends Number> SiAngle<NumericType> Subtract(SiAngle<NumericType> angleA, SiAngle<NumericType> angleB) {
    	return SiAngle.Add(angleA, angleB.Negate());
    }
    public static <NumericType extends Number> SiAngle<NumericType> Subtract(SiAngle<NumericType> angleA, double value) {
    	return SiAngle.Add(angleA, -value);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Multiply(SiAngle<NumericType> angleA, Double scalar) {
    	//Returns new SiAngle of the type of AngleA with the value of AngleA * scalar.
    	return new SiAngle<NumericType>(angleA.toDouble() * scalar, angleA.type);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Multiply(SiAngle<NumericType> angleA, SiAngle<NumericType> angleB) {
    	//Returns new SiAngle of the type of AngleA with the value of AngleA * AngleB.
    	return SiAngle.Multiply(angleA, angleB.ConvertToType(angleA.type).toDouble());
    }
    public static <NumericType extends Number> SiAngle<NumericType> Divide(SiAngle<NumericType> angleA, double value){
    	return SiAngle.Multiply(angleA, 1.0 / value);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Divide(SiAngle<NumericType> angleA, SiAngle<NumericType> angleB){
    	return SiAngle.Divide(angleA, angleB.ConvertToType(angleA.type).toDouble());
    }
    public static <NumericType extends Number> SiAngle<NumericType> Negate(SiAngle<NumericType> angle){
    	//Returns new SiAngle that is angle normalized.
    	return new SiAngle<NumericType>(-angle.toDouble(), angle.type);
    }
    public static <NumericType extends Number> SiAngle<NumericType> Normalize(SiAngle<NumericType> angle){
    	//Returns new SiAngle that is angle normalized.
    	return new SiAngle<NumericType>(angle.toDouble() % GetFullCircleValueOfType(angle.type), angle.type);
    }
    public static String GetAbbreviationOfType(AngleType type, boolean allowWideCharacters) {
        // Returns Abbreviation/Symbol based on AngleType.
        switch (type) {
            case AngleTypeRotations:
                return "tr";
            case AngleTypeDegrees:
                return "°";
            case AngleTypeRadians:
                return allowWideCharacters ? "π" : "rad";
            case AngleTypeGradians:
                return "g";
            case AngleTypeMinuteOfAngle:
                return "'";
            case AngleTypeSecondOfArc:
                return "\"";
            case AngleTypeMilliradians:
                return "mils";
            case AngleTypeMilliradiansNATO:
                return "mils(NATO)";
            case AngleTypeMilliradiansUSSR:
                return "mils(USSR)";
            default:
                throw new IllegalArgumentException("Unknown AngleType: " + type);
        }
    }
    public static <NumericType extends Number> String toString(SiAngle<NumericType> angle, boolean allowWideCharacters) {
        return angle.toDouble() + "" + GetAbbreviationOfType(angle.type, allowWideCharacters);
    }

    // Constructors
    public SiAngle() {
    	this(SiAngle.DEFAULT_VALUE);
    }
    public SiAngle(Double pValue) {
    	this(pValue, SiAngle.DEFAULT_ANGLE_TYPE);
    }
    @SuppressWarnings("unchecked")
	public SiAngle(Double pValue, AngleType pType) {
        this.value = (NumericType)pValue;
        this.type = pType;
        assert (this.value == pValue) : "SiDoubleAngle detected data loss on conversion from Double.";
    }
    public SiAngle(Float pValue) {
    	this(pValue, SiAngle.DEFAULT_ANGLE_TYPE);
    }
    @SuppressWarnings("unchecked")
	public SiAngle(Float pValue, AngleType pType) {
        this.value = (NumericType)pValue;
        this.type = pType;
        assert (this.value == pValue) : "SiDoubleAngle detected data loss on conversion from Float.";
    }
    public SiAngle(Long pValue) {
    	this(pValue, SiAngle.DEFAULT_ANGLE_TYPE);
    }
    @SuppressWarnings("unchecked")
	public SiAngle(Long pValue, AngleType pType) {
        this.value = (NumericType) pValue;
        this.type = pType;
        assert (this.value == pValue) : "SiDoubleAngle detected data loss on conversion from Long.";
    }
    public SiAngle(Integer pValue) {
    	this(pValue, SiAngle.DEFAULT_ANGLE_TYPE);
    }
    @SuppressWarnings("unchecked")
	public SiAngle(Integer pValue, AngleType pType) {
        this.value = (NumericType)pValue;
        this.type = pType;
        assert (this.value == pValue) : "SiDoubleAngle detected data loss on conversion from Int.";
    }
    public SiAngle(Short pValue) {
    	this(pValue, SiAngle.DEFAULT_ANGLE_TYPE);
    }
    @SuppressWarnings("unchecked")
	public SiAngle(Short pValue, AngleType pType) {
        this.value = (NumericType)pValue;
        this.type = pType;
        assert (this.value == pValue) : "SiDoubleAngle detected data loss on conversion from Short.";
    }

    // Methods

    // Functions
    public SiAngle<NumericType> ConvertToType(AngleType type) {
        // Possible data loss
        SiAngle<NumericType> tmp = ConvertToType(this, type);
        this.value = tmp.value;
        this.type = tmp.type;
        return this;
    }
    public SiAngle<NumericType> Add(SiAngle<NumericType> angle){
    	return Add(this, angle);
    }
    public SiAngle<NumericType> Negate(){
    	return Negate(this);
    }
    public SiAngle<NumericType> Normalized(){
    	return Normalize(this);
    }
    public double toDouble() {
        return (double) this.value;
    }
    public float toFloat() {
        return (float) this.value;
    }
    public long toLong() {
        return (long) this.value;
    }
    public int toInt() {
        return ( int) this.value;
    }
    public short toShort() {
        return (short) this.value;
    }
    public String toString(boolean allowWideCharacters) {
        return toString(this, allowWideCharacters);
    }
    @Override
    public String toString() {
        return this.toString(false);
    }
    //Events
    
}
