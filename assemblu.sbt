import AssemblyKeys._

assemblySettings

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case "workflow.conf"     => MergeStrategy.singleOrError
    case x => old(x)
  }
}