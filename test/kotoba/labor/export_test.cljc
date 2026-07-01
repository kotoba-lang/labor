(ns kotoba.labor.export-test
  (:require [clojure.test :refer [deftest is testing]]
            [kotoba.labor :as labor]
            [kotoba.labor.export :as ex]))
(deftest csv-export
  (let [csv (ex/contracts->csv [(labor/contract "C1" "worker" "employer" "nanny" :hourly 1500)])]
    (is (re-find #"contract_id,worker,role,wage_type,rate,currency" csv))
    (is (re-find #"C1,worker,nanny,hourly,1500" csv))))
(deftest json-export
  (let [j (ex/payrolls->json [(labor/payroll "P1" "worker" "2026-07" 12000 :deductions 2000)])]
    (is (re-find #"\"net\":10000" j))))
