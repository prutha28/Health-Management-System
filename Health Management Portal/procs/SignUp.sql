CREATE OR REPLACE PROCEDURE SignUp (
   PID              IN JSHARDA.PATIENT.PID%TYPE,
   DOB              IN JSHARDA.PATIENT.DOB%TYPE,
   NAME             IN JSHARDA.PATIENT.NAME%TYPE,
   ADDRESS          IN JSHARDA.PATIENT.ADDRESS%TYPE,
   GENDER           IN JSHARDA.PATIENT.GENDER%TYPE,
   CONTACT_NUMBER   IN JSHARDA.PATIENT.CONTACT_NUMBER%TYPE,
   PASSWORD         IN JSHARDA.PATIENT.PASSWORD%TYPE,
   CATEGORY         IN JSHARDA.PATIENT.CATEGORY%TYPE)
IS
--
-- Declare program variables as shown below
-- variable_name data_type
BEGIN
   INSERT INTO PATIENT ("PID",
                        "DOB",
                        "NAME",
                        "ADDRESS",
                        "GENDER",
                        "CONTACT_NUMBER",
                        "PASSWORD",
                        "CATEGORY")
        VALUES (PID,
                DOB,
                NAME,
                ADDRESS,
                GENDER,
                CONTACT_NUMBER,
                PASSWORD,
                CATEGORY);

   COMMIT;
END;                                                               --Procedure
/
