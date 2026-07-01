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


## Maturity

| | |
|---|---|
| Role | capability |
| Tests | 27 assertions, all green |
| Operator console (UI/UX) | yes |
| Export (CSV/JSON) | yes |
| Shared CSS design system | yes (css.core/operator-theme) |

## Contract

```clojure
(require '[kotoba.labor :as labor])

(labor/contract "C1" "worker" "employer" "nanny" :hourly 1500)
(labor/total-hours [(labor/timesheet "worker" "2026-07-01" 8)])
(labor/wages-for contract timesheet-entries)
(labor/payroll "P1" "worker" "2026-07" 21000 :deductions 2000)
```

## Operator console (UI/UX)

A read-only HTML dashboard renders contracts, timesheets and payroll (gross/deductions/net) for an operator. Built on
[`kotoba-lang/html`](https://github.com/kotoba-lang/html) (Hiccup→HTML) +
[`kotoba-lang/css`](https://github.com/kotoba-lang/css) (EDN→CSS). Pure data
→ markup; the console never exposes a write surface (no `<form>`/`<button>`)
— writes stay behind the governor.

```clojure
(require '[kotoba.labor.ui :as ui])

(ui/dashboard
  {:contracts [(labor/contract "C1" "worker" "employer" "nanny" :hourly 1500)]
   :timesheets [(labor/timesheet "worker" "2026-07-01" 8)]
   :payrolls [(labor/payroll "P1" "worker" "2026-07" 12000 :deductions 2000)]})
;; => "<html>...read-only · governor-gated...</html>"
```

## Export (CSV / JSON)

Audit-grade CSV (RFC-4180 quoting) and JSON (quote/backslash/newline
escaped) for contracts, timesheets and payroll.

```clojure
(require '[kotoba.labor.export :as ex])

(ex/contracts->csv contracts)
(ex/payrolls->csv payrolls)   ; gross/deductions/net
(ex/payrolls->json payrolls)
```

## Why

A household-employer operator must never pay a worker without a valid
contract, and must never net a payroll that differs from gross minus
documented deductions. `kotoba-labor` is the pure-data layer a
`PolicyGovernor` checks against; the actor (`cloud-itonami-9700`) decides
permission, the audit ledger records proof.

## License

Apache License 2.0.
