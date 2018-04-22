def call(nipkgVersion, vsVersion) {

   if(vsVersion == "2015") {
      vsVersion = "2015sp1"
   }

   def nipmAppPath = "C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"
   def controlFileText = readFile "control"
   def instructionsFileText = readFile "instructions"
   echo controlFileText

   def programFilesStagingDirectory = "data\\ProgramFiles_32\\VeriStand Steps for TestStand"
   echo programFilesStagingDirectory


   def replacementExpressionMap = ['veristand_version': vsVersion, 'nipkg_version': nipkgVersion] 
   def nipkgDir = "nipkg\\veristand${vsVersion}-steps-teststand$"
   def programFilesStagingSource = "built\\programFiles_32"
   def programFilesStagingDest = "${nipkgDir}\\data\\programFiles_32"
   def documentsStagingSource = "built\\documents"
   def documentsStagingDest = "${nipkgDir}\\data\\documents"
   def updatedControlText = controlFileText

   bat "(robocopy \"${programFilesStagingSource}\" \"${programFilesStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"
   bat "(robocopy \"${documentsStagingSource}\" \"${documentsStagingDest}\" /MIR /NFL /NDL /NJH /NJS /nc /ns /np) ^& exit 0"

   replacementExpressionMap.each { replacementExpression, replacementValue ->
      updatedControlText = updatedControlText.replaceAll("\\{${replacementExpression}\\}", replacementValue)
   }
   echo updatedControlText

   dir(nipkgDir){
      writeFile file:'control\\control', text: updatedControlText
      writeFile file:'data\\instructions', text: instructionsFileText
      writeFile file:'debian-binary', text: "2.0"
   }

   dir('built\\nipkg') {
      bat "\"${nipmAppPath}\" pack \"${nipkgDir}\""          
   }
}
