package plm.core.lang

/**
 * @author matthieu
 */
class ProgrammingLanguages(classLoader: ClassLoader) {
  val java: ProgrammingLanguage = new LangJava(classLoader, false)
  val python: ProgrammingLanguage = new LangPython(false)
  val scala: ProgrammingLanguage = new LangScala(classLoader, false)
  val blockly: ProgrammingLanguage = new LangBlockly(false)

  ProgrammingLanguage.registerSupportedProgLang(java)
  ProgrammingLanguage.registerSupportedProgLang(python)
  ProgrammingLanguage.registerSupportedProgLang(scala)
  ProgrammingLanguage.registerSupportedProgLang(blockly)

  def programmingLanguages(): Array[ProgrammingLanguage] = Array(java, python, scala, blockly)
  def defaultProgrammingLanguage(): ProgrammingLanguage = ProgrammingLanguage.defaultProgLang
  def getProgrammingLanguage(progLangName: String): ProgrammingLanguage = ProgrammingLanguage.getProgrammingLanguage(progLangName)
}
