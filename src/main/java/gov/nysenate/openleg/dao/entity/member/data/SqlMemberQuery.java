package gov.nysenate.openleg.dao.entity.member.data;

import gov.nysenate.openleg.dao.base.BasicSqlQuery;
import gov.nysenate.openleg.dao.base.SqlTable;

public enum SqlMemberQuery implements BasicSqlQuery
{
    /** --- Member --- */

    SELECT_MEMBER_SELECT_FRAGMENT(
        "SELECT sm.id AS session_member_id, sm.member_id, sm.lbdc_short_name, sm.session_year, sm.district_code,\n" +
        "       m.chamber, m.incumbent, p.id AS person_id, p.full_name, p.first_name, p.middle_name, p.last_name, p.suffix, " +
        "       p.img_name"
    ),
    SELECT_MEMBER_TABLE_FRAGMENT(
        "FROM " + SqlTable.SESSION_MEMBER + " sm\n" +
        "JOIN " + SqlTable.MEMBER + " m ON m.id = sm.member_id\n" +
        "JOIN " + SqlTable.PERSON + " p ON p.id = m.person_id\n"
    ),
    SELECT_MEMBER_FRAGMENT(
        SELECT_MEMBER_SELECT_FRAGMENT.sql + "\n" + SELECT_MEMBER_TABLE_FRAGMENT.sql
    ),
    SELECT_MEMBER_BY_ID_SQL(
        SELECT_MEMBER_FRAGMENT.sql + " WHERE sm.member_id = :memberId AND sm.alternate = false"
    ),
    SELECT_MEMBER_BY_ID_SESSION_SQL(
        SELECT_MEMBER_BY_ID_SQL.sql + " AND sm.session_year = :sessionYear"
    ),
    SELECT_MEMBER_BY_SESSION_MEMBER_ID_SQL(
        "SELECT smp.id AS session_member_id, smp.lbdc_short_name, sm.id, sm.member_id, sm.session_year, sm.district_code,\n" +
        "       m.chamber, m.incumbent, p.id AS person_id, p.full_name, p.first_name, p.middle_name, p.last_name, p.suffix, p.img_name" + "\n" +
        SELECT_MEMBER_TABLE_FRAGMENT.sql +
        "JOIN " + SqlTable.SESSION_MEMBER + " smp ON smp.member_id = sm.member_id AND smp.session_year = sm.session_year AND smp.alternate = FALSE\n" +
        "WHERE sm.id = :sessionMemberId"
    ),
    SELECT_MEMBER_BY_SHORTNAME_SQL(
        SELECT_MEMBER_FRAGMENT.sql + "\n" +
        //     We use the first 15 letters to compare due to how some of the source data is formatted.
        "WHERE substr(sm.lbdc_short_name, 1, 15) ILIKE substr(:shortName, 1, 15) AND m.chamber = :chamber::chamber " +
        "      AND sm.alternate = :alternate "
    ),
    SELECT_MEMBER_BY_SHORTNAME_SESSION_SQL(
         SELECT_MEMBER_BY_SHORTNAME_SQL.sql + " AND sm.session_year = :sessionYear"
    ),

    INSERT_UNVERIFIED_PERSON_SQL(
        "INSERT INTO " + SqlTable.PERSON + "\n" +
               "( full_name, first_name,   middle_name, last_name, email, prefix, suffix, verified)\n" +
        "VALUES (:fullName, :firstInitial, NULL,       :lastName,  NULL,  NULL,   NULL,   FALSE)\n" +
        "RETURNING id"
    ),
    INSERT_UNVERIFIED_MEMBER_SQL(
        "INSERT INTO " + SqlTable.MEMBER + "\n" +
               "( person_id,      chamber,              incumbent,  full_name)\n" +
        "VALUES (:personId, CAST(:chamber AS chamber), :incumbent, :fullName)\n" +
        "RETURNING id"
    ),
    INSERT_UNVERIFIED_SESSION_MEMBER_SQL(
        "INSERT INTO " + SqlTable.SESSION_MEMBER + "\n" +
               "( member_id, lbdc_short_name, session_year, district_code )\n" +
        "VALUES (:memberId, :lbdcShortName,  :sessionYear,  NULL )\n" +
        "RETURNING id"
    )
    ;

    private String sql;

    SqlMemberQuery(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}