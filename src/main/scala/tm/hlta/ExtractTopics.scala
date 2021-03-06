package tm.hlta

import tm.util.Arguments
import java.nio.file.Paths
import java.nio.file.Files
import tm.util.FileHelpers

object ExtractTopics {
  class BaseConf(args: Seq[String]) extends Arguments(args) {
    val outputDirectory = opt[String](default = Some("topic_output"),
      descr = "Output directory for extracted topic files (default: topic_output)")
    val title = opt[String](default = Some("Topic Tree"), descr = "Title in the topic tree")
    val name = trailArg[String](descr = "Name of files to be generated")
    val model = trailArg[String](descr = "Name of model file (e.g. model.bif)")
  }
  //trait Arguments extends  Argument

  class Conf(args: Seq[String]) extends BaseConf(args) {
    banner("Usage: tm.hlta.ExtractTopics [OPTION]... name model")

    verify
    checkDefaultOpts()
  }

  def main(args: Array[String]) {
    val conf = new Conf(args)

    val output = Paths.get(conf.outputDirectory())
    FileHelpers.mkdir(output)

    clustering.HLTAOutputTopics_html_Ltm.main(
      Array(conf.model(), conf.outputDirectory(), "no", "no", "7"));

    val topicFile = output.resolve("TopicsTable.html")
    RegenerateHTMLTopicTree.run(topicFile.toString(), conf.name(), conf.title())
  }
}

object ExtractNarrowTopics {
  class Conf(args: Seq[String]) extends ExtractTopics.BaseConf(args) {
    banner("Usage: tm.hlta.ExtractNarrowTopics [OPTION]... name model data")
    val data = trailArg[String](descr = "Data file (e.g. data.txt)")

    verify
    checkDefaultOpts()
  }

  def main(args: Array[String]) {
    val conf = new Conf(args)

    val output = Paths.get(conf.outputDirectory())
    FileHelpers.mkdir(output)

    tm.hlta.ExtractNarrowTopics_LCM.main(
      Array(conf.model(), conf.data(), conf.outputDirectory(), "no", "no", "7"));

    val topicFile = output.resolve("TopicsTable.html")
    RegenerateHTMLTopicTree.run(topicFile.toString(), conf.name(), conf.title())
  }
}