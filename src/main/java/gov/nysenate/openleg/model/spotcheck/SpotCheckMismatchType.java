package gov.nysenate.openleg.model.spotcheck;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import gov.nysenate.openleg.util.OutputUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static gov.nysenate.openleg.model.spotcheck.SpotCheckRefType.*;

/**
 * Enumeration of the different bill fields that we can check for data quality issues.
 */
public enum SpotCheckMismatchType
{
    /** --- General --- */

    REFERENCE_DATA_MISSING("Ref. Missing", SpotCheckRefType.values()),
    OBSERVE_DATA_MISSING("Data Missing", SpotCheckRefType.values()),

    /** --- Bill data mismatches --- */

    BILL_ACTION("Action", LBDC_DAYBREAK),
    BILL_ACTIVE_AMENDMENT("Amendment", LBDC_DAYBREAK, LBDC_SCRAPED_BILL),
    BILL_AMENDMENT_PUBLISH("Publish", LBDC_DAYBREAK),
    BILL_COSPONSOR("Co Sponsor", LBDC_DAYBREAK),
    BILL_FULLTEXT_PAGE_COUNT("Page Count", LBDC_DAYBREAK),
    BILL_TEXT_LINE_OFFSET("Text Line Offset", LBDC_SCRAPED_BILL),
    BILL_TEXT_CONTENT("Text Content", LBDC_SCRAPED_BILL),
    BILL_LAW_CODE_SUMMARY("Law/Summary", LBDC_DAYBREAK),
    BILL_LAW_SECTION("Law Section", LBDC_DAYBREAK),
    BILL_MEMO("Memo", LBDC_SCRAPED_BILL),
    BILL_MULTISPONSOR("Multi Sponsor", LBDC_DAYBREAK),
    BILL_SESSION_YEAR("Session Year", LBDC_DAYBREAK),
    BILL_SPONSOR("Sponsor", LBDC_DAYBREAK),
    BILL_TITLE("Title", LBDC_DAYBREAK),


    /** --- Active List data mismatches --- */

    LIST_CAL_DATE("Cal Date", LBDC_CALENDAR_ALERT),
    LIST_RELEASE_DATE_TIME("Release Time", LBDC_CALENDAR_ALERT),
    LIST_CALENDAR_MISMATCH("Calendar?", LBDC_CALENDAR_ALERT),
    LIST_ENTRY_MISMATCH("Cal Entry", LBDC_CALENDAR_ALERT),

    /** --- Agenda Committee Meeting info mismatches --- */

    AGENDA_BILL_LISTING("Bill List", LBDC_AGENDA_ALERT),
    AGENDA_CHAIR("Chair", LBDC_AGENDA_ALERT),
    AGENDA_MEETING_TIME("Meeting Time", LBDC_AGENDA_ALERT),
    AGENDA_LOCATION("Location", LBDC_AGENDA_ALERT),
    AGENDA_NOTES("Notes", LBDC_AGENDA_ALERT),

    /** --- Supplemental mismatches --- */
    SUPPLEMENTAL_CAL_DATE("Supplemental Calendar Date", LBDC_CALENDAR_ALERT),
    SUPPLEMENTAL_SECTION_TYPE("Supplemental Section", LBDC_CALENDAR_ALERT),
    SUPPLEMENTAL_ENTRY("Supplemental Entry", LBDC_CALENDAR_ALERT),

    /** --- Active list mismatches --- */
    ACTIVE_LIST_CAL_DATE("Active List Calendar Date", LBDC_CALENDAR_ALERT),
    ACTIVE_LIST_ENTRY("Active List Entry", LBDC_CALENDAR_ALERT),

    ;

    private String displayName;
    Set<SpotCheckRefType> refTypes;

    SpotCheckMismatchType(String displayName, SpotCheckRefType... refTypes) {
        this.displayName = displayName;
        this.refTypes = new HashSet<>(Arrays.asList(refTypes));
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<SpotCheckRefType> getRefTypes() {
        return refTypes;
    }

    private static final SetMultimap<SpotCheckRefType, SpotCheckMismatchType> refTypeMismatchMap = HashMultimap.create();

    static {
        for (SpotCheckMismatchType type : SpotCheckMismatchType.values()) {
            type.refTypes.forEach(refType -> refTypeMismatchMap.put(refType, type));
        }
    }

    public static Set<SpotCheckMismatchType> getMismatchTypes(SpotCheckRefType refType) {
        return refTypeMismatchMap.get(refType);
    }

    public static String getJsonMap() {
        return OutputUtils.toJson(EnumSet.allOf(SpotCheckMismatchType.class).stream()
                .collect(Collectors.toMap(SpotCheckMismatchType::name, SpotCheckMismatchType::getDisplayName)));
    }

    public static String getJsonReftypeMismatchMap() {
        return OutputUtils.toJson(refTypeMismatchMap.asMap());
    }
}