package nl.louisa.booking.company.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.louisa.booking.hotel.domain.RoomType;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CompanyPolicy implements Policy {
    private final String companyId;
    private final List<RoomType> roomTypesAllowed;

    @Override
    public String getId() {
        return "CP-" + companyId;
    }
}
