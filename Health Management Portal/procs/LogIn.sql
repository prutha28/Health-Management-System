CREATE OR REPLACE PROCEDURE LogIn (
   UserId   IN     JSHARDA.PATIENT.PID%TYPE,
   pwd      IN     JSHARDA.PATIENT.PASSWORD%TYPE,
   role     IN     NUMBER,
   RET         OUT NUMBER)
IS
   category   NUMBER (3);
   hs_count   NUMBER (4);
   cred       NUMBER (1);
BEGIN
   RET := 0;
   cred := 0;

   SELECT COUNT (*)
     INTO cred
     FROM jsharda.PATIENT
    WHERE PID = UserId AND PASSWORD = pwd;

   IF (cred <> 0)
   THEN
      IF (role = 1)
      THEN
         SELECT COUNT (*)
           INTO category
           FROM jsharda.PATIENT
          WHERE PID = UserId AND PASSWORD = pwd AND CATEGORY <> 'N';

         IF (category > 0)
         THEN
            RET := 1;
         END IF;
      ELSE
         SELECT COUNT (*)
           INTO category
           FROM jsharda.PATIENT
          WHERE PID = UserId AND PASSWORD = pwd AND CATEGORY = 'N';

         IF (category > 0)
         THEN
            RET := 2;
         ELSE
            SELECT COUNT (*)
              INTO hs_count
              FROM jsharda.HEALTH_SUPPORTER hs
             WHERE hs.HSID = UserId AND hs.ENDDATE IS NULL;

            IF (hs_count > 0)
            THEN
               RET := 3;
            ELSE
               RET := 4;
            END IF;
         END IF;
      END IF;
   ELSE
      RET := -1;
   END IF;

   create_low_frequency_alerts (UserId);
END;
/
