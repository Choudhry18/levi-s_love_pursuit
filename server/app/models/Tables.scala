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
   *  @param userId1 Database column user_id1 SqlType(int4), Default(None)
   *  @param userId2 Database column user_id2 SqlType(int4), Default(None)
   *  @param matchTime Database column match_time SqlType(timestamp), Default(None) */
  case class MatchRow(matchId: Int, userId1: Option[Int] = None, userId2: Option[Int] = None, matchTime: Option[java.sql.Timestamp] = None)
  /** GetResult implicit for fetching MatchRow objects using plain SQL queries */
  implicit def GetResultMatchRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[java.sql.Timestamp]]): GR[MatchRow] = GR{
    prs => import prs._
    MatchRow.tupled((<<[Int], <<?[Int], <<?[Int], <<?[java.sql.Timestamp]))
  }
  /** Table description of table match. Objects of this class serve as prototypes for rows in queries. */
  class Match(_tableTag: Tag) extends profile.api.Table[MatchRow](_tableTag, "match") {
    def * = (matchId, userId1, userId2, matchTime).<>(MatchRow.tupled, MatchRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(matchId), userId1, userId2, matchTime)).shaped.<>({r=>import r._; _1.map(_=> MatchRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column match_id SqlType(serial), AutoInc, PrimaryKey */
    val matchId: Rep[Int] = column[Int]("match_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id1 SqlType(int4), Default(None) */
    val userId1: Rep[Option[Int]] = column[Option[Int]]("user_id1", O.Default(None))
    /** Database column user_id2 SqlType(int4), Default(None) */
    val userId2: Rep[Option[Int]] = column[Option[Int]]("user_id2", O.Default(None))
    /** Database column match_time SqlType(timestamp), Default(None) */
    val matchTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("match_time", O.Default(None))

    /** Foreign key referencing Users (database name match_user_id1_fkey) */
    lazy val usersFk1 = foreignKey("match_user_id1_fkey", userId1, Users)(r => Rep.Some(r.userId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name match_user_id2_fkey) */
    lazy val usersFk2 = foreignKey("match_user_id2_fkey", userId2, Users)(r => Rep.Some(r.userId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Match */
  lazy val Match = new TableQuery(tag => new Match(tag))

  /** Entity class storing rows of table Message
   *  @param messageId Database column message_id SqlType(serial), AutoInc, PrimaryKey
   *  @param matchId Database column match_id SqlType(int4), Default(None)
   *  @param senderId Database column sender_id SqlType(int4), Default(None) */
  case class MessageRow(messageId: Int, matchId: Option[Int] = None, senderId: Option[Int] = None)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[Option[Int]]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries. */
  class Message(_tableTag: Tag) extends profile.api.Table[MessageRow](_tableTag, "message") {
    def * = (messageId, matchId, senderId).<>(MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(messageId), matchId, senderId)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column message_id SqlType(serial), AutoInc, PrimaryKey */
    val messageId: Rep[Int] = column[Int]("message_id", O.AutoInc, O.PrimaryKey)
    /** Database column match_id SqlType(int4), Default(None) */
    val matchId: Rep[Option[Int]] = column[Option[Int]]("match_id", O.Default(None))
    /** Database column sender_id SqlType(int4), Default(None) */
    val senderId: Rep[Option[Int]] = column[Option[Int]]("sender_id", O.Default(None))

    /** Foreign key referencing Match (database name message_match_id_fkey) */
    lazy val matchFk = foreignKey("message_match_id_fkey", matchId, Match)(r => Rep.Some(r.matchId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name message_sender_id_fkey) */
    lazy val usersFk = foreignKey("message_sender_id_fkey", senderId, Users)(r => Rep.Some(r.userId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table Preference
   *  @param preferenceId Database column preference_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4), Default(None)
   *  @param gender Database column gender SqlType(varchar), Length(20,true), Default(None)
   *  @param year Database column year SqlType(varchar), Length(20,true), Default(None)
   *  @param greekPreference Database column greek_preference SqlType(varchar), Length(200,true), Default(None)
   *  @param religion Database column religion SqlType(varchar), Length(50,true), Default(None)
   *  @param commitment Database column commitment SqlType(varchar), Length(50,true), Default(None)
   *  @param major Database column major SqlType(varchar), Length(100,true), Default(None) */
  case class PreferenceRow(preferenceId: Int, userId: Option[Int] = None, gender: Option[String] = None, year: Option[String] = None, greekPreference: Option[String] = None, religion: Option[String] = None, commitment: Option[String] = None, major: Option[String] = None)
  /** GetResult implicit for fetching PreferenceRow objects using plain SQL queries */
  implicit def GetResultPreferenceRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[String]]): GR[PreferenceRow] = GR{
    prs => import prs._
    PreferenceRow.tupled((<<[Int], <<?[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table preference. Objects of this class serve as prototypes for rows in queries. */
  class Preference(_tableTag: Tag) extends profile.api.Table[PreferenceRow](_tableTag, "preference") {
    def * = (preferenceId, userId, gender, year, greekPreference, religion, commitment, major).<>(PreferenceRow.tupled, PreferenceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(preferenceId), userId, gender, year, greekPreference, religion, commitment, major)).shaped.<>({r=>import r._; _1.map(_=> PreferenceRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column preference_id SqlType(serial), AutoInc, PrimaryKey */
    val preferenceId: Rep[Int] = column[Int]("preference_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4), Default(None) */
    val userId: Rep[Option[Int]] = column[Option[Int]]("user_id", O.Default(None))
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

    /** Foreign key referencing Users (database name preference_user_id_fkey) */
    lazy val usersFk = foreignKey("preference_user_id_fkey", userId, Users)(r => Rep.Some(r.userId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Preference */
  lazy val Preference = new TableQuery(tag => new Preference(tag))

  /** Entity class storing rows of table Profile
   *  @param profileId Database column profile_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4), Default(None)
   *  @param firstName Database column first_name SqlType(varchar), Length(50,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(50,true)
   *  @param birthdate Database column birthdate SqlType(date), Default(None)
   *  @param bio Database column bio SqlType(varchar), Length(500,true), Default(None)
   *  @param photoUrl Database column photo_url SqlType(varchar), Length(200,true), Default(None)
   *  @param gender Database column gender SqlType(varchar), Length(50,true), Default(None)
   *  @param year Database column year SqlType(varchar), Length(50,true), Default(None)
   *  @param greekAssociation Database column greek_association SqlType(varchar), Length(50,true), Default(None)
   *  @param religion Database column religion SqlType(varchar), Length(50,true), Default(None)
   *  @param commitment Database column commitment SqlType(varchar), Length(50,true), Default(None)
   *  @param major Database column major SqlType(varchar), Length(50,true), Default(None) */
  case class ProfileRow(profileId: Int, userId: Option[Int] = None, firstName: String, lastName: String, birthdate: Option[java.sql.Date] = None, bio: Option[String] = None, photoUrl: Option[String] = None, gender: Option[String] = None, year: Option[String] = None, greekAssociation: Option[String] = None, religion: Option[String] = None, commitment: Option[String] = None, major: Option[String] = None)
  /** GetResult implicit for fetching ProfileRow objects using plain SQL queries */
  implicit def GetResultProfileRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[Option[java.sql.Date]], e4: GR[Option[String]]): GR[ProfileRow] = GR{
    prs => import prs._
    ProfileRow.tupled((<<[Int], <<?[Int], <<[String], <<[String], <<?[java.sql.Date], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table profile. Objects of this class serve as prototypes for rows in queries. */
  class Profile(_tableTag: Tag) extends profile.api.Table[ProfileRow](_tableTag, "profile") {
    def * = (profileId, userId, firstName, lastName, birthdate, bio, photoUrl, gender, year, greekAssociation, religion, commitment, major).<>(ProfileRow.tupled, ProfileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(profileId), userId, Rep.Some(firstName), Rep.Some(lastName), birthdate, bio, photoUrl, gender, year, greekAssociation, religion, commitment, major)).shaped.<>({r=>import r._; _1.map(_=> ProfileRow.tupled((_1.get, _2, _3.get, _4.get, _5, _6, _7, _8, _9, _10, _11, _12, _13)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column profile_id SqlType(serial), AutoInc, PrimaryKey */
    val profileId: Rep[Int] = column[Int]("profile_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4), Default(None) */
    val userId: Rep[Option[Int]] = column[Option[Int]]("user_id", O.Default(None))
    /** Database column first_name SqlType(varchar), Length(50,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(50,varying=true))
    /** Database column last_name SqlType(varchar), Length(50,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(50,varying=true))
    /** Database column birthdate SqlType(date), Default(None) */
    val birthdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("birthdate", O.Default(None))
    /** Database column bio SqlType(varchar), Length(500,true), Default(None) */
    val bio: Rep[Option[String]] = column[Option[String]]("bio", O.Length(500,varying=true), O.Default(None))
    /** Database column photo_url SqlType(varchar), Length(200,true), Default(None) */
    val photoUrl: Rep[Option[String]] = column[Option[String]]("photo_url", O.Length(200,varying=true), O.Default(None))
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

    /** Foreign key referencing Users (database name profile_user_id_fkey) */
    lazy val usersFk = foreignKey("profile_user_id_fkey", userId, Users)(r => Rep.Some(r.userId), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Profile */
  lazy val Profile = new TableQuery(tag => new Profile(tag))

  /** Entity class storing rows of table Users
   *  @param userId Database column user_id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(50,true)
   *  @param password Database column password SqlType(varchar), Length(100,true)
   *  @param email Database column email SqlType(varchar), Length(100,true) */
  case class UsersRow(userId: Int, username: String, password: String, email: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (userId, username, password, email).<>(UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userId), Rep.Some(username), Rep.Some(password), Rep.Some(email))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(serial), AutoInc, PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(50,true) */
    val username: Rep[String] = column[String]("username", O.Length(50,varying=true))
    /** Database column password SqlType(varchar), Length(100,true) */
    val password: Rep[String] = column[String]("password", O.Length(100,varying=true))
    /** Database column email SqlType(varchar), Length(100,true) */
    val email: Rep[String] = column[String]("email", O.Length(100,varying=true))

    /** Uniqueness Index over (email) (database name users_email_key) */
    val index1 = index("users_email_key", email, unique=true)
    /** Uniqueness Index over (username) (database name users_username_key) */
    val index2 = index("users_username_key", username, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
