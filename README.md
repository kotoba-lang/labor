# kotoba-labor

[![CI](https://github.com/kotoba-lang/labor/actions/workflows/ci.yml/badge.svg)](https://github.com/kotoba-lang/labor/actions/workflows/ci.yml)

**Employment contracts, timesheets and payroll in pure Clojure.** A
[kotoba-lang](https://github.com/kotoba-lang) capability library for the
[`cloud-itonami-9700`](https://github.com/gftdcojp/cloud-itonami-9700)
community domestic-employment open business: employment contracts (hourly
or monthly), timesheet entries, wage calculation, and payroll records with
deductions.

No network, no I/O. Amounts are plain numbers in the smallest currency unit
(e.g. cents) — no BigDecimal assumption, keeping the library portable
`.cljc` across JVM / ClojureScript / SCI / GraalVM.

## Contract

```clojure
(require '[kotoba.labor :as labor])

(labor/contract "C1" "worker" "employer" "nanny" :hourly 1500)
(labor/total-hours [(labor/timesheet "worker" "2026-07-01" 8)])
(labor/wages-for contract timesheet-entries)
(labor/payroll "P1" "worker" "2026-07" 21000 :deductions 2000)
```

## Why

A household-employer operator must never pay a worker without a valid
contract, and must never net a payroll that differs from gross minus
documented deductions. `kotoba-labor` is the pure-data layer a
`PolicyGovernor` checks against; the actor (`cloud-itonami-9700`) decides
permission, the audit ledger records proof.

## License

Apache License 2.0.
