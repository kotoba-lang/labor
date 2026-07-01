(ns kotoba.labor.export
  "Operator-facing export for a domestic-employment actor.

  Renders contracts, timesheets and payroll to CSV and JSON for payroll audit
  and downstream reporting. Pure data → text: no network."
  (:require [clojure.string :as str]
            [kotoba.labor :as labor]))

(defn- csv-cell [v]
  (let [s (str (if (nil? v) "" v))]
    (if (re-find #"[\",\n]" s)
      (str "\"" (str/replace s "\"" "\"\"") "\"")
      s)))

(defn- csv-row [vals] (str/join "," (map csv-cell vals)))

(defn- json-str [v]
  (-> (str (if (nil? v) "" v))
      (str/replace "\\" "\\\\")
      (str/replace "\"" "\\\"")
      (str/replace "\n" "\\n")))

(defn contracts->csv [contracts]
  (str/join "\n"
    (cons (csv-row ["contract_id" "worker" "role" "wage_type" "rate" "currency"])
          (for [c contracts]
            (csv-row [(:contract/id c)
                      (:contract/worker c)
                      (:contract/role c)
                      (name (:contract/wage-type c))
                      (:contract/rate c)
                      (:contract/currency c)])))))

(defn timesheets->csv [timesheets]
  (str/join "\n"
    (cons (csv-row ["worker" "date" "hours" "break"])
          (for [t timesheets]
            (csv-row [(:ts/worker t)
                      (:ts/date t)
                      (:ts/hours t)
                      (or (:ts/break t) "")])))))

(defn payrolls->csv [payrolls]
  (str/join "\n"
    (cons (csv-row ["payroll_id" "worker" "period" "gross" "deductions" "net" "currency"])
          (for [p payrolls]
            (csv-row [(:payroll/id p)
                      (:payroll/worker p)
                      (:payroll/period p)
                      (:payroll/gross p)
                      (:payroll/deductions p)
                      (:payroll/net p)
                      (:payroll/currency p)])))))

(defn contracts->json [contracts]
  (str "["
       (str/join ","
                 (for [c contracts]
                   (str "{\"contract_id\":\"" (json-str (:contract/id c)) "\","
                        "\"worker\":\"" (json-str (:contract/worker c)) "\","
                        "\"role\":\"" (json-str (:contract/role c)) "\","
                        "\"wage_type\":\"" (name (:contract/wage-type c)) "\","
                        "\"rate\":" (or (:contract/rate c) 0) ","
                        "\"currency\":\"" (or (:contract/currency c) "USD") "\"}")))
       "]"))

(defn payrolls->json [payrolls]
  (str "["
       (str/join ","
                 (for [p payrolls]
                   (str "{\"payroll_id\":\"" (json-str (:payroll/id p)) "\","
                        "\"worker\":\"" (json-str (:payroll/worker p)) "\","
                        "\"period\":\"" (json-str (:payroll/period p)) "\","
                        "\"gross\":" (or (:payroll/gross p) 0) ","
                        "\"deductions\":" (or (:payroll/deductions p) 0) ","
                        "\"net\":" (or (:payroll/net p) 0) "}")))
       "]"))
