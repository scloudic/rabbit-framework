<#if dialect?contains("oracle") && autoincrement==true>
    @ID(keyType = GenerationType.SEQUENCE)
<#elseif autoincrement==true>
    @ID
<#else>
    @ID(keyType = GenerationType.MANUAL)
</#if>