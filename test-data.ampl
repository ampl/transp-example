set Plants := seattle san-diego;
set Markets := new-york chicago topeka;

param Capacity :=
    seattle   350
    san-diego 600;

param Demand :=
    new-york 325
    chicago  300
    topeka   275;

param Distance : new-york chicago topeka :=
    seattle        2.5      1.7     1.8
    san-diego      2.5      1.8     1.4;

param Freight := 90;
