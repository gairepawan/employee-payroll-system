# Employee Payroll System — Java 21

An educational Java payroll console project covering **OOP**, **data structures and algorithms**, **BigDecimal money arithmetic**, **JUnit 5**, and **GitHub Actions CI**.

> **Educational demonstration only.** The example withholding percentage is not a real tax calculation. Overtime rules, salary treatment, withholding, benefits, rounding, and compliance vary by jurisdiction. **Do not use this application to pay actual employees.**

## Current capabilities

- Register hourly and fixed-weekly-salary employees.
- Find employees by ID using a `HashMap`.
- Sort employees by name using an `ArrayList` and `Comparator`.
- Calculate regular hourly wages and overtime after 40 hours at 1.5× (demo policy).
- Calculate fixed weekly salary, example proportional withholding, flat additional deductions, and net pay.
- Calculate total company gross pay for a week.
- Validate IDs, rates, hours, deductions, and withholding percentage.
- Run seven JUnit 5 tests; automatically build and test with GitHub Actions.

**Not implemented:** database, user login, GUI, payslip PDF, time clock, actual tax rules, payroll calendars, or production deployment. Data is held only in memory.

## Requirements

- JDK 21
- Apache Maven 3.9+
- Git (optional, to clone repository)

Verify installation:

```bash
java -version
mvn -version
```

## Build and run

```bash
git clone https://github.com/gairepawan/employee-payroll-system.git
cd employee-payroll-system
mvn clean verify
java -cp target/classes com.portfolio.payroll.App
```

Or extract the ZIP project and run the Maven commands inside the directory containing `pom.xml`.

Expected output:

```text
WEEKLY PAYROLL (illustrative 10% withholding)
EMP-101 | Alex Rivera | gross $950.00 | net $830.00
EMP-102 | Jordan Lee | gross $1200.00 | net $1055.00
EMP-103 | Taylor Morgan | gross $950.00 | net $830.00
TOTAL GROSS: $3100.00
```

Run tests separately with `mvn test`.

## Project structure

```text
employee-payroll-system/
├── .github/workflows/ci.yml
├── .gitignore
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/portfolio/payroll/
    │   ├── App.java
    │   ├── Employee.java
    │   ├── EmployeeDirectory.java
    │   ├── Paycheck.java
    │   └── PayrollCalculator.java
    └── test/java/com/portfolio/payroll/
        └── PayrollCalculatorTest.java
```

## OOP and DSA

| Use case | Approach | Complexity |
| --- | --- | --- |
| Store/search employees by ID | `HashMap<String, Employee>` | Average O(1) |
| Sort employees alphabetically | Copy to `ArrayList`, `Comparator` sort | O(n log n) |
| Calculate a single paycheck | `BigDecimal` arithmetic | Constant number of operations (cost depends on number precision) |
| Calculate all employees' weekly gross pay | Iterate through employees | O(n) |

`Employee` and `Paycheck` are immutable Java records. `PayrollCalculator` is a stateless calculation class. `EmployeeDirectory` stores data in memory and provides lookup, ordering, and aggregate calculations.

## Payroll rules in this educational version

- **Hourly employees:** regular time through 40 hours, 1.5× overtime for hours beyond 40, per weekly pay period.
- **Salaried employees:** `rate` is their full fixed **weekly** salary; entered hours do not change it.
- **Gross pay:** rounded to cents with `HALF_UP`.
- **Withholding:** gross × a caller-supplied example decimal (e.g., `0.10` for 10%), rounded to cents.
- **Other deductions:** one flat, nonnegative amount, rounded to cents.
- **Net pay:** gross − example withholding − deductions. Negative net pay is rejected.

Actual payroll tax calculations and regulatory requirements are **out of scope**.

## CI/CD

`.github/workflows/ci.yml` runs `mvn --batch-mode clean verify` on pushes to `main` and pull requests targeting `main`. It sets up Temurin Java 21 and caches Maven dependencies. This implements **continuous integration**; deployment is a future phase.

## Git workflow

```bash
git switch -c feature/payroll-report
# make changes and write tests
mvn clean verify
git add .
git commit -m "feat: add payroll summary report"
git push -u origin feature/payroll-report
```

Open a pull request and review CI results before merging.

## Roadmap

1. Interactive console menu and employee edit/remove operations.
2. Timesheets, pay periods, payroll history, and richer payroll reports.
3. Spring Boot REST API and PostgreSQL persistence.
4. Authentication, authorization, audit trail, and API tests.
5. Web dashboard and eventual deployment.

## Troubleshooting

- `java` or `mvn` not recognized: install JDK 21 / Maven and confirm `PATH` and `JAVA_HOME`.
- Class not found: run `mvn clean verify` before starting the app.
- Failing tests: review the JUnit output locally and the GitHub Actions logs.

## Security and license

Use fictional employee names and pay amounts. Do not commit real employee salaries, identifiers, credentials, or secrets. No license has been selected; default copyright restrictions apply.
