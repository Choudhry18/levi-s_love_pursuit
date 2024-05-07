package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile", 
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/dating?user=choudhry347&password=password",
    "./server/app/", 
    "models", None, None, true, false
  )
}