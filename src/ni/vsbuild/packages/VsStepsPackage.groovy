package ni.vsbuild.packages

class VsStepsPackage extends AbstractPackage {

   def nipkgVersion
   def tsVersions
   def vsVersion

   VsStepsPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.nipkgVersion = packageInfo.get('version')
      this.tsVersions = packageInfo.get('teststand_versions')
      this.vsVersion = lvVersion
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Package version: $nipkgVersion
         TestStand versions: $tsVersions
         """.stripIndent()

      script.echo packageInfo
      script.vsStepsPackage(nipkgVersion, tsVersions, payloadDir, vsVersion)

   }
}
