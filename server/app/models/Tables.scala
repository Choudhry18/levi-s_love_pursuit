package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = slick.jdbc.PostgresProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Match.schema ++ Message.schema ++ Preference.schema ++ Profile.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Match
   *  @param matchId Database column match_id SqlType(serial), AutoInc, PrimaryKey
   *  @param username1 Database column username1 SqlType(varchar), Length(50,true)
   *  @param username2 Database column username2 SqlType(varchar), Length(50,true) */
  case class MatchRow(matchId: Int, username1: String, username2: String)
  /** GetResult implicit for fetching MatchRow objects using plain SQL queries */
  implicit def GetResultMatchRow(implicit e0: GR[Int], e1: GR[String]): GR[MatchRow] = GR{
    prs => import prs._
    MatchRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table match. Objects of this class serve as prototypes for rows in queries. */
  class Match(_tableTag: Tag) extends profile.api.Table[MatchRow](_tableTag, "match") {
    def * = (matchId, username1, username2).<>(MatchRow.tupled, MatchRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(matchId), Rep.Some(username1), Rep.Some(username2))).shaped.<>({r=>import r._; _1.map(_=> MatchRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column match_id SqlType(serial), AutoInc, PrimaryKey */
    val matchId: Rep[Int] = column[Int]("match_id", O.AutoInc, O.PrimaryKey)
    /** Database column username1 SqlType(varchar), Length(50,true) */
    val username1: Rep[String] = column[String]("username1", O.Length(50,varying=true))
    /** Database column username2 SqlType(varchar), Length(50,true) */
    val username2: Rep[String] = column[String]("username2", O.Length(50,varying=true))

    /** Foreign key referencing Users (database name match_username1_fkey) */
    lazy val usersFk1 = foreignKey("match_username1_fkey", username1, Users)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name match_username2_fkey) */
    lazy val usersFk2 = foreignKey("match_username2_fkey", username2, Users)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Match */
  lazy val Match = new TableQuery(tag => new Match(tag))

  /** Entity class storing rows of table Message
   *  @param messageId Database column message_id SqlType(serial), AutoInc, PrimaryKey
   *  @param matchId Database column match_id SqlType(int4)
   *  @param senderUsername Database column sender_username SqlType(varchar), Length(50,true)
   *  @param messageText Database column message_text SqlType(varchar), Length(1000,true)
   *  @param timestamp Database column timestamp SqlType(timestamp) */
  case class MessageRow(messageId: Int, matchId: Int, senderUsername: String, messageText: String, timestamp: java.sql.Timestamp)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries. */
  class Message(_tableTag: Tag) extends profile.api.Table[MessageRow](_tableTag, "message") {
    def * = (messageId, matchId, senderUsername, messageText, timestamp).<>(MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(messageId), Rep.Some(matchId), Rep.Some(senderUsername), Rep.Some(messageText), Rep.Some(timestamp))).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column message_id SqlType(serial), AutoInc, PrimaryKey */
    val messageId: Rep[Int] = column[Int]("message_id", O.AutoInc, O.PrimaryKey)
    /** Database column match_id SqlType(int4) */
    val matchId: Rep[Int] = column[Int]("match_id")
    /** Database column sender_username SqlType(varchar), Length(50,true) */
    val senderUsername: Rep[String] = column[String]("sender_username", O.Length(50,varying=true))
    /** Database column message_text SqlType(varchar), Length(1000,true) */
    val messageText: Rep[String] = column[String]("message_text", O.Length(1000,varying=true))
    /** Database column timestamp SqlType(timestamp) */
    val timestamp: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("timestamp")

    /** Foreign key referencing Match (database name message_match_id_fkey) */
    lazy val matchFk = foreignKey("message_match_id_fkey", matchId, Match)(r => r.matchId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name message_sender_username_fkey) */
    lazy val usersFk = foreignKey("message_sender_username_fkey", senderUsername, Users)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table Preference
   *  @param preferenceId Database column preference_id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(50,true)
   *  @param gender Database column gender SqlType(varchar), Length(20,true), Default(None)
   *  @param year Database column year SqlType(varchar), Length(20,true), Default(None)
   *  @param greekPreference Database column greek_preference SqlType(varchar), Length(200,true), Default(None)
   *  @param religion Database column religion SqlType(varchar), Length(50,true), Default(None)
   *  @param commitment Database column commitment SqlType(varchar), Length(50,true), Default(None)
   *  @param major Database column major SqlType(varchar), Length(100,true), Default(None) */
  case class PreferenceRow(preferenceId: Int, username: String, gender: Option[String] = None, year: Option[String] = None, greekPreference: Option[String] = None, religion: Option[String] = None, commitment: Option[String] = None, major: Option[String] = None)
  /** GetResult implicit for fetching PreferenceRow objects using plain SQL queries */
  implicit def GetResultPreferenceRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[PreferenceRow] = GR{
    prs => import prs._
    PreferenceRow.tupled((<<[Int], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table preference. Objects of this class serve as prototypes for rows in queries. */
  class Preference(_tableTag: Tag) extends profile.api.Table[PreferenceRow](_tableTag, "preference") {
    def * = (preferenceId, username, gender, year, greekPreference, religion, commitment, major).<>(PreferenceRow.tupled, PreferenceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(preferenceId), Rep.Some(username), gender, year, greekPreference, religion, commitment, major)).shaped.<>({r=>import r._; _1.map(_=> PreferenceRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column preference_id SqlType(serial), AutoInc, PrimaryKey */
    val preferenceId: Rep[Int] = column[Int]("preference_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(50,true) */
    val username: Rep[String] = column[String]("username", O.Length(50,varying=true))
    /** Database column gender SqlType(varchar), Length(20,true), Default(None) */
    val gender: Rep[Option[String]] = column[Option[String]]("gender", O.Length(20,varying=true), O.Default(None))
    /** Database column year SqlType(varchar), Length(20,true), Default(None) */
    val year: Rep[Option[String]] = column[Option[String]]("year", O.Length(20,varying=true), O.Default(None))
    /** Database column greek_preference SqlType(varchar), Length(200,true), Default(None) */
    val greekPreference: Rep[Option[String]] = column[Option[String]]("greek_preference", O.Length(200,varying=true), O.Default(None))
    /** Database column religion SqlType(varchar), Length(50,true), Default(None) */
    val religion: Rep[Option[String]] = column[Option[String]]("religion", O.Length(50,varying=true), O.Default(None))
    /** Database column commitment SqlType(varchar), Length(50,true), Default(None) */
    val commitment: Rep[Option[String]] = column[Option[String]]("commitment", O.Length(50,varying=true), O.Default(None))
    /** Database column major SqlType(varchar), Length(100,true), Default(None) */
    val major: Rep[Option[String]] = column[Option[String]]("major", O.Length(100,varying=true), O.Default(None))

    /** Foreign key referencing Users (database name preference_username_fkey) */
    lazy val usersFk = foreignKey("preference_username_fkey", username, Users)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Preference */
  lazy val Preference = new TableQuery(tag => new Preference(tag))

  /** Entity class storing rows of table Profile
   *  @param profileId Database column profile_id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(50,true)
   *  @param firstName Database column first_name SqlType(varchar), Length(50,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(50,true)
   *  @param bio Database column bio SqlType(varchar), Length(500,true), Default(None)
   *  @param photo Database column photo SqlType(bytea), Default(None)
   *  @param gender Database column gender SqlType(varchar), Length(50,true), Default(None)
   *  @param year Database column year SqlType(varchar), Length(50,true), Default(None)
   *  @param greekAssociation Database column greek_association SqlType(varchar), Length(50,true), Default(None)
   *  @param religion Database column religion SqlType(varchar), Length(50,true), Default(None)
   *  @param commitment Database column commitment SqlType(varchar), Length(50,true), Default(None)
   *  @param major Database column major SqlType(varchar), Length(50,true), Default(None) */
  case class ProfileRow(profileId: Int, username: String, firstName: String, lastName: String, bio: Option[String] = None, photo: Option[Array[Byte]] = None, gender: Option[String] = None, year: Option[String] = None, greekAssociation: Option[String] = None, religion: Option[String] = None, commitment: Option[String] = None, major: Option[String] = None)
  /** GetResult implicit for fetching ProfileRow objects using plain SQL queries */
  implicit def GetResultProfileRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Array[Byte]]]): GR[ProfileRow] = GR{
    prs => import prs._
    ProfileRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<?[String], <<?[Array[Byte]], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table profile. Objects of this class serve as prototypes for rows in queries. */
  class Profile(_tableTag: Tag) extends profile.api.Table[ProfileRow](_tableTag, "profile") {
    def * = (profileId, username, firstName, lastName, bio, photo, gender, year, greekAssociation, religion, commitment, major).<>(ProfileRow.tupled, ProfileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(profileId), Rep.Some(username), Rep.Some(firstName), Rep.Some(lastName), bio, photo, gender, year, greekAssociation, religion, commitment, major)).shaped.<>({r=>import r._; _1.map(_=> ProfileRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profile_id SqlType(serial), AutoInc, PrimaryKey */
    val profileId: Rep[Int] = column[Int]("profile_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(50,true) */
    val username: Rep[String] = column[String]("username", O.Length(50,varying=true))
    /** Database column first_name SqlType(varchar), Length(50,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(50,varying=true))
    /** Database column last_name SqlType(varchar), Length(50,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(50,varying=true))
    /** Database column bio SqlType(varchar), Length(500,true), Default(None) */
    val bio: Rep[Option[String]] = column[Option[String]]("bio", O.Length(500,varying=true), O.Default(None))
    /** Database column photo SqlType(bytea), Default(None) */
    val photo: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("photo", O.Default(None))
    /** Database column gender SqlType(varchar), Length(50,true), Default(None) */
    val gender: Rep[Option[String]] = column[Option[String]]("gender", O.Length(50,varying=true), O.Default(None))
    /** Database column year SqlType(varchar), Length(50,true), Default(None) */
    val year: Rep[Option[String]] = column[Option[String]]("year", O.Length(50,varying=true), O.Default(None))
    /** Database column greek_association SqlType(varchar), Length(50,true), Default(None) */
    val greekAssociation: Rep[Option[String]] = column[Option[String]]("greek_association", O.Length(50,varying=true), O.Default(None))
    /** Database column religion SqlType(varchar), Length(50,true), Default(None) */
    val religion: Rep[Option[String]] = column[Option[String]]("religion", O.Length(50,varying=true), O.Default(None))
    /** Database column commitment SqlType(varchar), Length(50,true), Default(None) */
    val commitment: Rep[Option[String]] = column[Option[String]]("commitment", O.Length(50,varying=true), O.Default(None))
    /** Database column major SqlType(varchar), Length(50,true), Default(None) */
    val major: Rep[Option[String]] = column[Option[String]]("major", O.Length(50,varying=true), O.Default(None))

    /** Foreign key referencing Users (database name profile_username_fkey) */
    lazy val usersFk = foreignKey("profile_username_fkey", username, Users)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Profile */
  lazy val Profile = new TableQuery(tag => new Profile(tag))

  /** Entity class storing rows of table Users
   *  @param username Database column username SqlType(varchar), PrimaryKey, Length(50,true)
   *  @param password Database column password SqlType(varchar), Length(100,true)
   *  @param email Database column email SqlType(varchar), Length(100,true) */
  case class UsersRow(username: String, password: String, email: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[String], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (username, password, email).<>(UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(username), Rep.Some(password), Rep.Some(email))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(varchar), PrimaryKey, Length(50,true) */
    val username: Rep[String] = column[String]("username", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column password SqlType(varchar), Length(100,true) */
    val password: Rep[String] = column[String]("password", O.Length(100,varying=true))
    /** Database column email SqlType(varchar), Length(100,true) */
    val email: Rep[String] = column[String]("email", O.Length(100,varying=true))

    /** Uniqueness Index over (email) (database name users_email_key) */
    val index1 = index("users_email_key", email, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
