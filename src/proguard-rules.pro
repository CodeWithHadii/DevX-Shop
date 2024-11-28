# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.devxlabs.devxshop.DevxShop {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/devxlabs/devxshop/repack'
-flattenpackagehierarchy
-dontpreverify
