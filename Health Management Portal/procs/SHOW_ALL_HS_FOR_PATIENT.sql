CREATE OR REPLACE PROCEDURE SHOW_ALL_HS_FOR_PATIENT (
   patient_id                 IN     JSHARDA.PATIENT.PID%TYPE,
   cursor_health_supporters      OUT SYS_REFCURSOR)
AS
BEGIN
   OPEN cursor_health_supporters FOR
      SELECT hs.HSID,
             p.NAME,
             hs.SUPPORTER_TYPE,
             hs.AUTHDATE,
             p.ADDRESS,
             p.CONTACT_NUMBER,
             p.GENDER,
             p.DOB
        FROM JSHARDA.HEALTH_SUPPORTER hs, JSHARDA.PATIENT p
       WHERE     hs.PATIENT_PID = patient_id
             AND (hs.ENDDATE IS NULL OR hs.ENDDATE > CURRENT_TIMESTAMP)
             AND hs.HSID = p.PID;
END;
/
