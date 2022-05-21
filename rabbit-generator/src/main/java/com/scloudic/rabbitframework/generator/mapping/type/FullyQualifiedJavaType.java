package com.scloudic.rabbitframework.generator.mapping.type;

public class FullyQualifiedJavaType implements
        Comparable<FullyQualifiedJavaType> {
    private static final String JAVA_LANG = "java.lang";
    private static FullyQualifiedJavaType intInstance = null;
    private static FullyQualifiedJavaType stringInstance = null;
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
    private static FullyQualifiedJavaType objectInstance = null;
    private static FullyQualifiedJavaType dateInstance = null;
    private String shortName;
    private String fullName;
    private String packageName;
    private boolean primitive = true;

    public FullyQualifiedJavaType(String fullTypeSpecification) {
        super();
        parse(fullTypeSpecification);
    }


    /**
     * This method returns the fully qualified name - including any generic type
     * parameters
     *
     * @return Returns the fullyQualifiedName.
     */
    public String getFullName() {
        return fullName;
    }


    /**
     * @return Returns the packageName.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @return Returns the shortName - including any type arguments.
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return Returns the primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }

    public static final FullyQualifiedJavaType getIntInstance() {
        if (intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int");
        }
        return intInstance;
    }

    public static final FullyQualifiedJavaType getNewMapInstance() {
        return new FullyQualifiedJavaType("java.util.Map");
    }

    public static final FullyQualifiedJavaType getNewListInstance() {
        return new FullyQualifiedJavaType("java.util.List");
    }

    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        return new FullyQualifiedJavaType("java.util.HashMap");
    }

    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        return new FullyQualifiedJavaType("java.util.ArrayList");
    }

    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        return new FullyQualifiedJavaType("java.util.Iterator");
    }

    public static final FullyQualifiedJavaType getStringInstance() {
        if (stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String");
        }

        return stringInstance;
    }

    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("java.lang.Boolean");
        }

        return booleanPrimitiveInstance;
    }

    public static final FullyQualifiedJavaType getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object");
        }

        return objectInstance;
    }

    public static final FullyQualifiedJavaType getDateInstance() {
        if (dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date");
        }

        return dateInstance;
    }

    public int compareTo(FullyQualifiedJavaType other) {
        return getFullName().compareTo(other.getFullName());
    }

    private void parse(String fullTypeSpecification) {
        String typeSpecification = fullTypeSpecification.trim();
        fullName = typeSpecification.trim();
        shortName = fullName;
        if (fullName.contains(".")) {
            packageName = getPackage(fullName);
            shortName = fullName
                    .substring(packageName.length() + 1);
            int index = shortName.lastIndexOf('.');
            if (index != -1) {
                shortName = shortName.substring(index + 1);
            }
            if (JAVA_LANG.equals(packageName)) {
                primitive = true;
            } else {
                primitive = false;
            }
        }
    }

    private static String getPackage(String baseQualifiedName) {
        int index = baseQualifiedName.lastIndexOf('.');
        return baseQualifiedName.substring(0, index);
    }
}
