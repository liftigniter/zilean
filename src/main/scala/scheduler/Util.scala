package scheduler

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{S3ObjectSummary, ObjectListing}
import scala.collection.JavaConversions.{collectionAsScalaIterable => asScala}
import scala.sys.process.Process

/**
 * Created by guanguan on 7/30/14.
 */
object S3Util {

  lazy val s3 = new AmazonS3Client(new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY")))

  def exists(path: String): Boolean = {
    map(s3, "pm-archives", path)(_.getKey).exists(_.startsWith(path))
  }

  private def map[T](s3: AmazonS3Client, bucket: String, prefix: String)(f: (S3ObjectSummary) => T) = {

    def scan(acc:List[T], listing:ObjectListing): List[T] = {
      val summaries = asScala[S3ObjectSummary](listing.getObjectSummaries())
      val mapped = (for (summary <- summaries) yield f(summary)).toList

      if (!listing.isTruncated) mapped.toList
      else scan(acc ::: mapped, s3.listNextBatchOfObjects(listing))
    }
    scan(List(), s3.listObjects(bucket, prefix))
  }

}

object CmdUtil {

  def run(cmd: String): Boolean = {
    val pb = Process(cmd)
    val exitCode = pb.!
    if (exitCode == 0)
      true
    else
      false
  }
}

object PrintUtil {
  def apply(s: String) {
    println("########\t" + s)
  }
}