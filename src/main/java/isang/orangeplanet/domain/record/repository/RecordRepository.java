package isang.orangeplanet.domain.record.repository;

import isang.orangeplanet.domain.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
}
