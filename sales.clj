


; (ns tutorial.core
;   (:gen-class))
;;;;;;; customer-table;;;;;;;;;;;;

(defn load-customer []
  (def data (slurp "cust.txt"))
  (def map_list (clojure.string/split data #"\n"))
  (def person_list (hash-map))
  (doseq [item map_list]
       (def map_det (clojure.string/split item #"\|"))
       (def id (nth map_det 0))
       (def name_1 (nth map_det 1))
       (def address (nth map_det 2))
       (def phone (nth map_det 3))
       (def person_info {:name name_1 :address address :number phone})
       (def temp_person(hash-map (Integer. id) person_info))
       (def person_list (merge person_list temp_person))

  )

   (def final_list (into (sorted-map) person_list))
   ; (println (get (get final_list 1) :name))
   ; (println final_list)
  )

;;;;;;;;product-table;;;;;;;;;;

(defn load-product []
  (def data2 (slurp "prod.txt"))
  (def map_list2 (clojure.string/split data2 #"\n"))
  (def product_list (hash-map))
  (doseq [item2 map_list2]
       (def map_det2 (clojure.string/split item2 #"\|"))
       (def product_id (nth map_det2 0))
       (def product_name (nth map_det2 1))
       ; (def address2 (nth map_det 2))
       (def price (nth map_det2 2))
       (def product_info {:product-name product_name  :price price})
       (def temp_product(hash-map (Integer. product_id) product_info))
       (def product_list (merge product_list temp_product))

  )

    (def final_list2 (into (sorted-map) product_list))
   ; (println (get (get final_list2 1) :product-name))
   ; (println final_list2)
  )

;;;;;;;;sales table;;;;;;;;;;;;;;;

(defn load-sales []
  (def data3 (slurp "sales.txt"))
  (def map_list3 (clojure.string/split data3 #"\n"))
  (def sales_list (hash-map))
  (doseq [item3 map_list3]
       (def map_det3 (clojure.string/split item3 #"\|"))
       (def sales_id (nth map_det3 0))
       (def customer_id (nth map_det3 1))
       (def product_id (nth map_det3 2))
       (def amount (nth map_det3 3))
       (def sales_info {:customer_id customer_id :product_id product_id  :amount amount})
       (def temp_sales(hash-map (Integer. sales_id) sales_info))
       (def sales_list (merge sales_list temp_sales))

  )

   (def final_list3 (into (sorted-map) sales_list))

   (def x (get (get final_list3 1) :customer_id))
   (println x)
  )

;;;;;;;;;;;main function;;;;;;;;;;;;;;;
;;;;;final_list is Customer list;;;;;;
;;;;;final_list2 is Product list;;;;;;
;;;;;final_list3 is Sales list;;;;;;;;

(defn -main[]

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;First Option Customer Table;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn customer []
    (println "-----------Customer Table--------------")
    (doseq [item final_list]
      (println (key item)":""[""\""(get (get final_list (key item)) :name)"\"""\""(get (get final_list (key item)) :address)"\"""\""(get (get final_list (key item)) :number)"\"""]")
      )
    (-main))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Second Option Product Table;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn product []
    (println "-----------Product Table--------------")
    (doseq [item2 final_list2]
      (println (key item2)":""[""\""(get (get final_list2 (key item2)) :product-name)"\"""\""(get (get final_list2 (key item2)) :price)"\"""]")
    )
    (-main))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Third option Sales Table;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (defn sales []
    (println "-----------Sales Table--------------")
    (doseq [item3 final_list3]

      (def the_name (get (get final_list (Integer. (get (get final_list3 (key item3)) :customer_id))) :name) )

      (def the_product (get (get final_list2 (Integer. (get (get final_list3 (key item3)) :product_id))) :product-name) )

      (def amount_of_product (get (get final_list3 (key item3)) :amount))

      (println (key item3)":" "[""\""the_name"\"""\""the_product"\"""\""amount_of_product"\"""]" )
      )
    (-main))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Fourth option Total Sales;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;if customer isn't found;;;;;;;
(defn goback []
  (println "Customer Not Found!")
  (-main)
)


;;;;;;;;get the hash-map for item and amount;;;;;;;;;

(defn send_sales_info [productID numbers]
    (doseq [item6 final_list2]
      (if (= productID (key item6))
        (def pricing (get (get final_list2 (key item6)) :price))
      )
    )
    (def temporary(hash-map (format "%.2f" (Float/parseFloat pricing)) numbers)) ;;;;;;;; store the price and amount in hash-map;;;;;;;
    (def multiplied_numbers (merge multiplied_numbers temporary))

)



;;;;;;;;;find the customer in sales table and send productid and amount to send_sales_info;;;;;;;;;

(defn send_name_id [n]
 (def this (first n))
    (doseq [item5 final_list3]
      (def temp_name_id (get (get final_list3 (key item5)) :customer_id))

      (def star (Integer. (get (get final_list3 (key item5)) :product_id)))

      (def star2 (Integer. (get (get final_list3 (key item5)) :amount)))

      (if (= (Integer. temp_name_id) this )
        (send_sales_info star star2)        ;;;;;;;;;;sales_info having product_id and amount;;;;;;
      )


      )
)



(defn total_sales []
    (println "Please Enter Name:")
    (def input_name (read-line))
    (def multiplied_numbers (hash-map))  ;;;;; calling an empty hash-map to store price and amount from send sales info function;;;;;
    (def fool `())
    (doseq [item4 final_list]
      (def temp_name (get (get final_list (key item4)) :name))
      (if (= input_name temp_name)
        (def fool (conj fool (key item4)))      ;;;;;;find customer_id from the given name;;;;;;;
      )

      )

    (if (= 1 (count fool))
      (send_name_id (take 1 fool)) ;;;;;customer_id is sent to sales table using send_name_id;;;;;;;;;
      (goback)
    )

    (def list_final `())
    (doseq [item7 multiplied_numbers]
      (def multiplied (format "%.2f" (* (Float/parseFloat (key item7)) (val item7)))) ;;;;;hash-map key value used to get total price for each item;;;;;;
      (def list_final (conj list_final multiplied))       ;;;;;that each item total price is put in a list;;;;;;;
    )


  (def abc `(0.00))
  (doseq [item8 list_final]
    (def dine (Float/parseFloat item8))

    (def taking (first abc) )             ;;;;;;; add all item of list;;;;;;;

    (def r (+ dine taking))
    (def abc(conj abc r))
  )
  (println "------Total Sales for Customer-------")
  (println input_name ":""$"(format "%.2f" (first abc)) )
  (-main)

)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Fifth option Total Product;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn goback2 []
  (println "Product Not found!!!")
  (-main)
)

;;;;;;;;;;;get amount from sales table using product id;;;;;;;;;;;;;;;

(defn send_product_id [i]
  (def this2 (first i))
  (def amount_list `()) ;;;;;;;;;;;;;;declare an empty list to store amount each time product id is found in sales table;;;;;;;;;;
  (doseq [item10 final_list3]
          (def star3 (Integer. (get (get final_list3 (key item10)) :product_id)))

          (def star4 (Integer. (get (get final_list3 (key item10)) :amount)))
          (if (= (Integer. star3) this2)
            (def amount_list (conj amount_list star4))        ;;;;;;;;;;amount is stored in the list;;;;;;
          )

  )
)
(defn total_product []
  (println "Enter product name:")
  (def input_product (read-line))

  (def fool2 `())
  (doseq [item9 final_list2]
    (def temp_product_name (get (get final_list2 (key item9)) :product-name))
    (if (= input_product temp_product_name)
      (def fool2 (conj fool2 (key item9)))      ;;;;;;find product_id from the input product name;;;;;;;
    )
  )
  (if (= 1 (count fool2))
    (send_product_id (take 1 fool2)) ;;;;;;;;; send product_id to sales table;;;;;;;;;;
    (goback2)
  )
  (def total_amount `(0))
  (doseq [item11 amount_list]
    (def dine2 item11)

    (def take_sum (first total_amount) )             ;;;;;;; add all item of amount list;;;;;;;

    (def r2 (+ dine2 take_sum))
    (def total_amount(conj total_amount r2))
  )
  (println "------Total Count for Product-------")
  (println input_product ":"(first total_amount) )
  (-main)


)




  (defn exitfunc []
    (println "Good-Bye")
    (System/exit 0)
  )


  (println "*** Sales Menu ***")
  (println "-----------------------------")
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (println "\nEnter an option?")

  (let [input (read-line)]
    (cond (= input "1") (customer)
          (= input "2") (product)
          (= input "3") (sales)
          (= input "4") (total_sales)
          (= input "5")(total_product)
          (= input "6")(exitfunc)
          :else (-main)))


)

(load-customer)
(load-product)
(load-sales)
(-main)
