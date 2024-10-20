package com.ecs.sgws.poc.romsdocuments;

import com.ecs.sgws.poc.romsdocuments.model.Address;
import com.ecs.sgws.poc.romsdocuments.model.Company;
import com.ecs.sgws.poc.romsdocuments.model.CompanyRepository;
import com.ecs.sgws.poc.romsdocuments.model.Phones;
import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

@Log4j2
@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "com.ecs.sgws.poc.romsdocuments.model")
public class RomsDocumentsApplication implements CommandLineRunner {

  @Autowired
  private CompanyRepository companyRepo;

  public static void main(String[] args) {
    SpringApplication.run(RomsDocumentsApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    init();
    queryByName();
    queryByNumber();
    queryByTagsPaginated();
    queryByMultipleTagsPaginated();
    queryByGeoFilter();
    queryByGeoAndPaginate();
    queryNearByTagsPaginate();
    System.exit(0);
  }

  public void queryNearByTagsPaginate() {
    // Generated Query : INCORRECT, Issue : [1] tags param is not bounded. [2] Location filter is missing from the query.
    // "FT.AGGREGATE" "idx:company" "@tags:{$tags}" "LIMIT" "0" "2" "SORTBY" "2" "numberOfEmployees" "DESC" "DIALECT" "1"
    log.info("Near by paginated companies with tags = {}",
        companyRepo.findNearByTags(new Point(-122.066540, 37.377690),
            new Distance(100, Metrics.KILOMETERS),
            Set.of("reliable"),
            PageRequest.of(0, 2, Sort.by(Direction.DESC, "numberOfEmployees"))));
  }

  public void queryByMultipleTagsPaginated() {
    // Generated Query : INCORRECT, Issue : limit sub-clause is missing in the query.
    // "FT.SEARCH" "idx:company" "@tags:{reliable} @id:{01JAMJ97XBAXX5T24QCVHJ3DSZ}" "SORTBY" "yearFounded" "ASC" "DIALECT" "1"
    log.info("Paginated Companies with multiple tags fields = {}",
        companyRepo.findByTagsAndIds(Set.of("reliable"),
            Set.of("01JAMJ97XBAXX5T24QCVHJ3DSZ"),
            PageRequest.of(0, 10, Sort.by(Direction.ASC, "yearFounded"))));
  }

  public void queryByTagsPaginated() {
    // Generated Query : INCORRECT, Issue : limit sub-clause is missing in the query.
    // "FT.SEARCH" "idx:company" "@tags:{reliable}" "SORTBY" "yearFounded" "ASC" "DIALECT" "1"
    log.info("Paginated Companies with tags = {}",
        companyRepo.findByTags(Set.of("reliable"), PageRequest.of(0, 10, Sort.by(Direction.ASC, "yearFounded"))));
  }

  public void queryByNumber() {
    // Generated Query : CORRECT
    // "FT.SEARCH" "idx:company" "@numberOfEmployees:[526 526]" "LIMIT" "0" "10000" "DIALECT" "1"
    log.info("Number of employees = {}", companyRepo.findByNumberOfEmployees(526));

    // Generated Query : CORRECT
    // "idx:company" "@numberOfEmployees:[0 1000]" "LIMIT" "0" "10000" "DIALECT" "1"
    log.info("Number of employees between = {}", companyRepo.findByNumberOfEmployeesBetween(0, 1000));
  }

  public void queryByGeoAndPaginate() {
    // Generated Query : CORRECT
    // "FT.SEARCH" "idx:company" "@location:[-122.06654 37.37769 100.0 km]" "SORTBY" "yearFounded" "ASC" "LIMIT" "0" "2" "DIALECT" "1"
    log.info("geo paginated result = {}",
        companyRepo.findByLocationNear(
            new Point(-122.066540, 37.377690),
            new Distance(100, Metrics.KILOMETERS),
            PageRequest.of(0, 2, Sort.by(Direction.ASC, "yearFounded"))));
  }

  public void queryByGeoFilter() {
    // Generated Query : CORRECT
    // "FT.SEARCH" "idx:company" "@location:[-122.06654 37.37769 100.0 km]" "LIMIT" "0" "10000" "DIALECT" "1"
    log.info("geo result = {}",
        companyRepo.findByLocationNear(new Point(-122.066540, 37.377690), new Distance(100, Metrics.KILOMETERS)));
  }

  public void queryByName() {
    // Generated Query : CORRECT
    // "FT.SEARCH" "idx:company" "@name:Redis" "LIMIT" "0" "10000" "DIALECT" "1"
    Optional<Company> result = companyRepo.findOneByName("Redis");
    log.info("Result = {}", result);
    result.ifPresentOrElse(
        company -> log.info("Company found: {}", company),
        () -> log.info("Company not found")
    );

  }

  public void init() {
    log.info("Started initialization");
    companyRepo.deleteAll();
    Company redis = Company.of("Redis", "https://redis.com", new Point(-122.066540, 37.377690), 526, 2011);
    redis.setAddress(Address.builder().city("San Jose").build());
    redis.setPhonesList(List.of(Phones.builder().number("+1 100-908-8765").build()));
    redis.setTags(Set.of("fast", "scalable", "reliable"));

    Company microsoft = Company.of("Microsoft", "https://microsoft.com", new Point(-122.124500, 47.640160), 182268,
        1975);
    microsoft.setAddress(Address.builder().city("Redmond").build());
    microsoft.setPhonesList(List.of(Phones.builder().number("+1 100-908-1234").build()));
    microsoft.setTags(Set.of("innovative", "reliable"));

    Company tesla = Company.of("Tesla", "https://tesla.com", new Point(-122.124500, 47.640160), 182268, 1975);
    tesla.setAddress(Address.builder().city("Austin").build());
    tesla.setPhonesList(List.of(Phones.builder().number("+1 100-908-5678").build()));
    tesla.setTags(Set.of("innovative", "reliable", "electric"));

    companyRepo.save(redis);
    companyRepo.save(microsoft);
    companyRepo.save(tesla);
    log.info("Completed initialization");
  }
}
