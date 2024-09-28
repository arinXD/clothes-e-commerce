let express = require('express')
let app = express()
let bodyParser = require('body-parser')
let mysql = require('mysql')

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({
    extended: true
}))



app.get('/', function (req, res) {
    return res.send({
        error: true,
        message: 'Unicode Web API'
    })
})

// connection config
let dbConn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'unicode',

})

// connect
dbConn.connect()

app.get("/type/product/:type_id",function(req, res){
    let type_id = req.params.type_id
    if(type_id==0){
        dbConn.query("SELECT * FROM `product`",function(error,results){
            if(error) throw error
            return res.send(results)
        })
    }else{
        dbConn.query("SELECT * FROM `product` WHERE product_type = ?",type_id,function(error,results){
            if(error) throw error
            return res.send(results)
        })
    }
})

/////////////////////////////////////////// order ///////////////////////////////////////////////////////
// SELECT
// 	`order`.`id` as 'order_id', pay_status, transport_fee, user_address_id, `order`.created_at, 
//     order_datial.id as 'order_datail_id',order_datial.amount, order_datial.product_price,
//     order_datial.price_all, product.id as 'product_id', product.product_name, product.detail, product.photo, size.size
// FROM `order`, `order_datial`, `product`, `size`
// WHERE (
//     `order`.`id` = `order_datial`.`order_id`
//     AND `order_datial`.`product_id` = `product`.`id`
//     AND `order_datial`.`size_id` = `size`.`id`
// )
// AND `order`.`user_id` = ?
app.get('/order/:order_id', function (req, res) {
    let id = req.params.order_id
    dbConn.query("SELECT * FROM `order` WHERE id = ?", id, function (error, results) {
        if (error) throw error
        return res.send(results[0])
    })

})
app.get('/order/history/:user_id', function (req, res) {
    let id = req.params.user_id
    // dbConn.query("SELECT `order`.`id` as 'order_id', pay_status, transport_fee, user_address_id, `order`.created_at, order_datial.id as 'order_datial_id',order_datial.amount, order_datial.product_price, order_datial.price_all, product.id as 'product_id', product.product_name, product.detail, product.photo, size.size FROM `order`, `order_datial`, `product`, `size` WHERE ( `order`.`id` = `order_datial`.`order_id` AND `order_datial`.`product_id` = `product`.`id` AND `order_datial`.`size_id` = `size`.`id` ) AND `order`.`user_id` = ? AND `order`.`pay_status` LIKE 'success'", id , function(error, results){
    dbConn.query("SELECT * FROM `order` WHERE user_id = ? AND `pay_status` LIKE 'success'", id, function (error, results) {
        if (error) throw error
        return res.send(results)
    })
})
app.get("/order/detail/:order_id", function (req, res) {
    let id = req.params.order_id
    dbConn.query("SELECT * FROM `order_datial` WHERE `order_id` = ?", id, function (error, results) {
        if (error) throw error
        return res.send(results)
    })
})
app.get('/order/product/:user_id', function (req, res) {
    let id = req.params.user_id
    dbConn.query("SELECT `order`.`id` as 'order_id', pay_status, transport_fee, user_address_id, `order`.created_at, order_datial.id as 'order_datial_id',order_datial.amount, order_datial.product_price, order_datial.price_all, product.id as 'product_id', product.product_name, product.detail, product.photo, size.size FROM `order`, `order_datial`, `product`, `size` WHERE ( `order`.`id` = `order_datial`.`order_id` AND `order_datial`.`product_id` = `product`.`id` AND `order_datial`.`size_id` = `size`.`id` ) AND `order`.`user_id` = ? AND `order`.`pay_status` LIKE 'none'", id, function (error, results) {
        if (error) throw error
        return res.send(results)
    })
})
app.get('/order/address/:address_id', function (req, res) {
    let id = req.params.address_id
    dbConn.query("SELECT * FROM `user_address` WHERE `id` = ?", id, function (error, results) {
        if (error) throw error
        return res.send(results[0])
    })
})
app.get('/order/credit/:credit_card_id', function (req, res) {
    let id = req.params.credit_card_id
    dbConn.query("SELECT * FROM `credit_card` WHERE `id` = ?", id, function (error, results) {
        if (error) throw error
        return res.send(results[0])
    })
})
app.get('/myOrder/:id', function (req, res) {
    let id = req.params.id
    dbConn.query('SELECT * FROM `order` WHERE user_id = ? AND pay_status LIKE "none"', id, function (error, results, fields) {
        if (error) throw error;
        if (results[0] == null) {
            dbConn.query("INSERT INTO `order` (`user_id`, `transport_fee`) VALUES (?, 0)", id)
            dbConn.query('SELECT * FROM `order` WHERE user_id = ? AND pay_status LIKE "none"', id, function (error, results) {
                if (error) throw error;
                return res.send(results[0])
            })
        } else {
            return res.send(results[0]);
        }
    })
})

// delete order detail
app.delete('/delete/order/:order_datial_id', function (req, res) {
    let id = req.params.order_datial_id;
    dbConn.query('DELETE FROM `order_datial` WHERE id = ?', id, function (error, results, fields) {
        if (error) throw error;
        return res.send({
            error: false,
            message: 'order_detail has been deleted!'
        });
    });
});

app.post('/addOrder', function (req, res) {
    let data = req.body
    dbConn.query('SELECT * FROM `order` WHERE user_id = ? AND pay_status LIKE "none"', data.user_id, function (error, results) {
        // return res.send(results)
        if (results[0] == null) {
            dbConn.query("INSERT INTO `order` (`user_id`, `transport_fee`) VALUES (?, 0)", data.user_id, function (error, results) {
                // return res.send(results)
            })
        }
        dbConn.query("INSERT INTO order_datial (amount, product_price, price_all, order_id, product_id, size_id) VALUES (?, ?, ?, (SELECT id FROM `order` WHERE user_id = ? AND pay_status LIKE 'none'), ?, ?)", [
            data.amount,
            data.product_price,
            data.price_all,
            data.user_id,
            data.product_id,
            data.size_id
        ], function (error, results) {
            if (error) throw error
            return res.send(results)
        })
    })
})
// order/complete/{order_id}
app.put('/order/complete/:order_id', function (req, res) {
    let order_id = req.params.order_id
    dbConn.query('UPDATE `order` SET `pay_status` = "success" WHERE id = ?', [order_id], function (error, results) {
        if (error) throw error

        dbConn.query('INSERT INTO `transport` (detail, transport_status_id, order_id) VALUES (?, ?, ?)', ["ทำการสั่งซื้อสำเร็จ", 1, order_id], function (error, results) {
            if (error) throw error
            return res.send(results)
        })
    })
})
app.put('/order/address/:order_id/:address_id', function (req, res) {
    let order_id = req.params.order_id;
    let address_id = req.params.address_id;

    dbConn.query('UPDATE `order` SET user_address_id = ? WHERE id = ?', [address_id, order_id], function (error, results, fields) {
        if (error) throw error;
        return res.send({
            error: false,
            message: 'address_id has been updated!',
            data: results
        });
    });
});
app.put('/order/credit/:order_id/:credit_card_id', function (req, res) {
    let order_id = req.params.order_id;
    let credit_card_id = req.params.credit_card_id;

    dbConn.query('UPDATE `order` SET `credit_card_id` = ? WHERE id = ?', [credit_card_id, order_id], function (error, results, fields) {
        if (error) throw error;
        return res.send({
            error: false,
            message: 'credit_card_id has been updated!',
            data: results
        });
    });
});

///////////////////////////////////////////address///////////////////////////////////////////////////////
app.get('/allAddress/:id', function (req, res) {
    let address_id = req.params.id;
    dbConn.query('SELECT * FROM user_address WHERE user_id = ?', address_id, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    })
});

app.post('/address', function (req, res) {
    var address = req.body

    if (!address) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide user_address'
        });

    }
    dbConn.query("INSERT INTO user_address SET ?", address, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });
});
app.get('/address/:id', function (req, res) {
    let address_id = req.params.id;
    if (!address_id) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide Student id'
        })
    }
    dbConn.query("SELECT * FROM user_address WHERE id = ?", address_id, function (error, results, fields) {
        if (error) throw error;
        if (results[0]) {
            return res.send(results[0]);
        } else {
            return res.status(400).send({
                error: true,
                message: 'user_address id Not Found!'
            });
        }
    });
});

app.put('/address/:id', function (req, res) {
    let address_id = req.params.id;
    let address = req.body
    if (!address_id || !address) {
        return res.status(400).send({
            error: true,
            message: 'Please provide user_address id and user_address data'
        });
    }

    dbConn.query('UPDATE user_address SET ? WHERE id = ?', [address, address_id], function (error, results, fields) {
        if (error) throw error;

        return res.send({
            error: false,
            message: 'address_id has been updated!'
        });
    });
});


app.delete('/address/:id', function (req, res) {
    let address_id = req.params.id;
    if (!address_id) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide address_id id'
        })
    }
    dbConn.query('DELETE FROM user_address WHERE id = ?', address_id, function (error, results, fields) {
        if (error) throw error;

        return res.send({
            error: false,
            message: 'user_address has been deleted!'
        });
    });
});


////////////////////////////////////////cradit///////////////////////////////////////////////////////
app.get('/credit/:user_id', function (req, res) {
    let id = req.params.user_id;
    dbConn.query('SELECT * FROM credit_card WHERE user_id = ?', id, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    })
});

app.post('/cradit', function (req, res) {
    var cradit = req.body

    if (!cradit) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide credit_card'
        });

    }
    dbConn.query("INSERT INTO credit_card SET ?", cradit, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });
});

app.get('/cradit/:id', function (req, res) {
    let cradit_id = req.params.id;
    if (!cradit_id) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide Student id'
        })
    }
    dbConn.query("SELECT * FROM credit_card WHERE id = ?", cradit_id, function (error, results, fields) {
        if (error) throw error;
        if (results[0]) {
            return res.send(results[0]);
        } else {
            return res.status(400).send({
                error: true,
                message: 'credit_card id Not Found!'
            });
        }
    });
});

app.put('/cradit/:id', function (req, res) {
    let cradit_id = req.params.id;
    let cradit = req.body
    if (!cradit_id || !cradit) {
        return res.status(400).send({
            error: true,
            message: 'Please provide credit_card id and cradit_id data'
        });
    }

    dbConn.query('UPDATE credit_card SET ? WHERE id = ?', [cradit, cradit_id], function (error, results, fields) {
        if (error) throw error;

        return res.send({
            error: false,
            message: 'cradit_id has been updated!'
        });
    });
});


app.delete('/cradit/:id', function (req, res) {
    let cradit_id = req.params.id;
    if (!cradit_id) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide credit_card id'
        })
    }
    dbConn.query('DELETE FROM credit_card WHERE id = ?', cradit_id, function (error, results, fields) {
        if (error) throw error;

        return res.send({
            error: false,
            message: 'credit_card has been deleted!'
        });
    });
});
//////////////////////////////////////////////////transport//////////////////////////////////////////////////////
app.get('/alltransport', function (req, res) {
    dbConn.query('SELECT * FROM transport', function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    })
});
app.post('/transport', function (req, res) {
    var transport = req.body

    if (!transport) {
        return res.status(400).send({
            error: true,
            message: 'Please Provide transport'
        });

    }
    dbConn.query("INSERT INTO transport SET ?", transport, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });
});
/////////////////////////////////////////////////////////////////////////////////////////////
app.get('/allorderss', function (req, res) {
    dbConn.query('SELECT * FROM `order`', function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    })
});
app.get('/alltransport_status', function (req, res) {
    dbConn.query('SELECT * FROM transport_status', function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    })
});



////////////////////////////////////////////////// user //////////////////////////////////////////////////////
app.post('/register', function (req, res) {
    let user = req.body

    if (!user) {
        return res.status(400).send({
            error: true,
            message: 'Please provide user'
        });
    }

    dbConn.query("INSERT INTO user SET ?", user, function (error, results, fields) {
        if (error) throw error;

        if (results) {
            return res.send({
                "status": 1,
            })
        } else {
            return res.status(401).send({
                "status": 0,
            })
        }

        // return res.send(results);
    });
});

app.post('/login', function (req, res) {
    let user = req.body;

    if (!user || !(user.user_name || user.email) || !user.user_password) {
        return res.status(400).send({
            error: true,
            message: 'Please provide valid user credentials'
        });
    }

    let query = "SELECT * FROM user WHERE (user_name = ? OR email = ?) AND user_password = ?";
    let values = [user.user_name || user.email, user.user_name || user.email, user.user_password];

    dbConn.query(query, values, function (error, results, fields) {
        if (error) throw error;

        if (results.length > 0) {
            let user = results[0]
            return res.send({
                "status": 1,
                "user_id": user.user_id,
                "user_type": user.user_type,
                "user_name": user.user_name,
                "email": user.email,
                "gender": user.gender,
                "birthday": user.birthday,
            })
        } else {
            return res.send({
                "status": 0,
                "user_id": user.user_id,
                "user_type": user.user_type,
                "user_name": user.user_name,
                "email": user.email,
                "gender": user.gender,
                "birthday": user.birthday,
            })
        }
    })
})

////////////////////////////////////////////////// admin //////////////////////////////////////////////////////

app.get("/product/size/:product_id", function (req, res) {
    let id = req.params.product_id;
    dbConn.query('SELECT `size`.`id`, `size`.`size` FROM `product_size`,`size` WHERE `product_size`.`size_id` = `size`.`id` AND`product_id` = ?', id, function (error, results) {
        if (error) throw error
        return res.send(results)
    })
})

app.get('/allproduct', function (req, res) {
    dbConn.query("SELECT * FROM product", function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });
});

app.get("/product/type", function (req, res) {
    dbConn.query("SELECT * FROM product_type", function (error, results) {
        if (error) throw error
        return res.send(results)
    })
})

app.post('/product', function (req, res) {

    var product = req.body

    if (!product) {
        return res.status(400).send({
            error: true,
            message: "Please provide product"
        });
    }

    dbConn.query("INSERT INTO product SET ?", product, function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });

});

app.get('/product/:id', function (req, res) {
    let id = req.params.id;
    if (!id) {
        return res.status(400).send({
            error: true,
            message: 'Please provide product id'
        });
    }
    dbConn.query('SELECT * FROM product WHERE id = ?', id, function (error, results, fields) {
        if (error) throw error;
        if (results[0]) {
            return res.send(results[0]);
        } else {
            return res.status(400).send({
                error: true,
                message: 'Product id Not Found!!'
            });
        }
    });
});

app.put('/product/update', function (req, res) {
    // @Field("product_name") product_name: String,
    // @Field("price") price: Int,
    // @Field("detail") detail: String,
    // @Field("photo") photo: String,
    // @Field("amount") amount: Int,
    // @Field("product_type") product_type: Int,

    // UPDATE product SET product_name = ?, price = ?, detail = ?, photo = ?, amount = ?, product_type = ?  WHERE id = ?

    let id = req.body.id;
    let product = req.body;

    if (!id || !product) {
        return res.status(400).send({
            error: user,
            message: 'Please provide product id and product data '
        });
    }
    dbConn.query("UPDATE product SET product_name = ?, price = ?, detail = ?, photo = ?, amount = ?, product_type = ?  WHERE id = ?",
        [
            product.product_name,
            product.price,
            product.detail,
            product.photo,
            product.amount,
            product.product_type,
            id
        ],
        function (error, results) {
            if (error) throw error
            return res.send({
                error: false,
                data: results,
                message: 'product been updated successfully.'
            });
        });

})
// // add  INSERT INTO product_size VALUES (?, ?) (product_id, size_id)
// delete_size.forEach(e => {
//     dbConn.query("DELETE FROM `product_size` WHERE product_id = ? AND size_id = ?", [id, e])
// })
// add_size.forEach(e => {
//     dbConn.query("INSERT INTO product_size VALUES (?, ?)", [id, e])
// })
// // delete   DELETE FROM `product_size` WHERE product_id = 2 AND size_id = 1

    // let add_size = req.body.add_size;
    // let delete_size = req.body.delete_size;

app.post("/add/type", function(req, res){
    let name = req.body.type_name
    // INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...);
    dbConn.query("INSERT INTO product_type (type_name) VALUES (?)", name, function(error, results){
        if(error) throw error
        return res.send(results)
    })
})

app.post("/add/product/size", function (req, res){
    let product_id = req.body.product_id;
    dbConn.query("DELETE FROM `product_size` WHERE product_id = ?", product_id, function (error) {
        if (error) throw error
    })
    let size_id = req.body.size_id;
    dbConn.query("INSERT INTO product_size VALUES (?, ?)", [product_id, size_id], function(error, results){
        if(error) throw error
        return res.send(results)
    })
})
app.post("/delete/product/size", function (req, res){
    let product_id = req.body.product_id;
    let size_id = req.body.size_id;
    dbConn.query("DELETE FROM `product_size` WHERE product_id = ? AND size_id = ?", [product_id, size_id], function(error, results){
        if(error) throw error
        return res.send(results)
    })
})

app.delete('/product/:id', function (req, res) {
    let id = req.params.id;
    if (!id) {
        return res.status(400).send({
            error: user,
            message: 'Please provide product data '
        });
    }

    dbConn.query("DELETE FROM product WHERE id = ?", id, function (error, results, fields) {
        if (error) throw error;
        return res.send({
            error: false,
            data: results,
            message: 'Product been deleted successfully.'
        });

    });
})

////////////////////////////////////////////////// favorite //////////////////////////////////////////////////////
app.get('/fav/add/:user_id/:product_id', function (req, res) {
    let id = req.params.user_id
    let pid = req.params.product_id

    dbConn.query("SELECT `favorite`.`id` FROM `user`, `favorite` WHERE `favorite`.`user_id` = `user`.`user_id` AND `user`.`user_id` = ?", id, function (error, results) {
        fav_id = results[0]
        if (fav_id == null) {
            dbConn.query("INSERT INTO `favorite` (`user_id`) VALUES (?)", id, function(error, results){
                if (error) throw error
                return res.send(results)
            })
        }else{
            dbConn.query("SELECT * FROM `product_favorite` WHERE product_id = ? AND favorite_id = (SELECT id FROM favorite WHERE user_id = ?)",[pid, id],function(error, results){
                if (error) throw error
                if(results.length>0){
                    return res.send(results[0])
                }else{
                    dbConn.query("INSERT INTO `product_favorite` (`pv_id`, `product_id`, `favorite_id`) VALUES (NULL, ?, (SELECT id FROM favorite WHERE user_id = ?))",[pid, id],function(error, results){
                        if(error) throw error
                        return res.send(results)
                    })
                }
            })
        }
    })

})

app.delete('/fav/product/:pv_id', function (req, res) {
    let id = req.params.pv_id

    if (!id) {
        return res.status(400).send({
            error: user,
            message: 'Please provide product fav data '
        });
    }

    dbConn.query("DELETE FROM `product_favorite` WHERE `pv_id` = ?", id, function (error, results, fields) {
        if (error) throw error;
        return res.send({
            error: false,
            message: "fav has been successfully"
        })

    })
})

app.get('/fav/product/:user_id', function (req, res) {
    let uId = req.params.user_id;

    if (!uId) {
        return res.status(400).send({
            error: user,
            message: 'Please provide product ID'
        });
    }


    dbConn.query("SELECT `product_favorite`.`pv_id`,`product`.`id`, `product`.`product_name`, `product`.`detail`, `product`.`price`, `product`.`photo`, `product`.`amount` FROM `user`, `favorite`, `product_favorite`, `product` WHERE (`user`.`user_id` = `favorite`.`user_id` AND `favorite`.`id`=`product_favorite`.`favorite_id` AND `product_favorite`.`product_id` = `product`.`id`) AND `favorite`.`user_id` = ?", [uId],
        function (error, results, fields) {
            if (error) throw error;
            return res.send(results)
        })
})

////////////////////////////////////////////////// product //////////////////////////////////////////////////////
app.get('/products/all', function (req, res) {
    dbConn.query('SELECT * FROM `product`', function (error, result) {
        if (error) throw error
        return res.send(result)
    })
})

//last update
app.get('/products/last', function (req, res) {
    dbConn.query('SELECT * FROM `product` ORDER BY `created_at` DESC LIMIT 1;', function (error, result) {
        if (error) throw error
        return res.send(result)
    })
})

//get product & size
app.get('/products/size/:id', function (req, res) {
    let pId = req.params.id;

    if (!pId) {
        return res.status(400).send({
            error: user,
            message: 'Please provide product ID'
        });
    }

    dbConn.query("SELECT `size`.`id`, `size`.`size` FROM `product`,`product_size`,`size` WHERE (`product`.`id` = `product_size`.`product_id` AND `product_size`.`size_id` = `size`.`id`) AND `product`.`id` = ?", [pId],
        function (error, results, fields) {

            if (error) throw error;
            return res.send(results)
        })
})

app.get("/order/history/product/:order_id",function(req, res){
    let order_id = req.params.order_id
    dbConn.query("SELECT product.product_name, order_datial.amount, product.price, order_datial.price_all FROM `order_datial`,`product` WHERE order_datial.product_id = product.id AND order_id = ?",order_id,function(error, results){
        if(error) throw error
        return res.send(results)
    })
})

app.get("/transport/detail/:order_id",function(req, res){
    let order_id = req.params.order_id
    dbConn.query("SELECT transport_status.title, `transport`.`detail`, `transport`.`created_at`, transport_status.photo FROM `transport`, `transport_status` WHERE transport.transport_status_id = transport_status.id AND order_id = ?",order_id,function(error, results){
        if(error) throw error
        return res.send(results)
    })
})

app.get("/receive/order/transport/:receive_status",function(req, res){
    let receive_status = req.params.receive_status
    if(receive_status == "N"){
        dbConn.query("SELECT * FROM `order` WHERE receive_status = 'N' ORDER BY `order`.`id` ASC",function(error, results){
            if(error) throw error
            return res.send(results)
        })
    }else{
        dbConn.query("SELECT * FROM `order` WHERE receive_status = 'Y' ORDER BY `order`.`id` ASC",function(error, results){
            if(error) throw error
            return res.send(results)
        })
    }
})

app.get("/find/price/all/:order_id",function(req, res){
    let order_id = req.params.order_id
    dbConn.query("SELECT sum(price_all) as price_all FROM `order_datial` WHERE order_id = ?",order_id,function(error, results){
        if(error) throw error
        return res.send(results[0])
    })
})

app.get("/add/success/receive/order/:order_id",function(req, res){
    let order_id = req.params.order_id
    dbConn.query("UPDATE `order` SET receive_status = 'Y' WHERE id = ?",order_id,function(error, results){
        if(error) throw error
        return res.send(results)
    })
})

app.post("/add/detail/transport",function(req, res){
    let detail = req.body.detail
    let transport_status_id = req.body.transport_status_id
    let order_id = req.body.order_id
    dbConn.query("INSERT INTO `transport` (`detail`, `transport_status_id`, `order_id`) VALUES (?, ?, ?)",[
        detail,
        transport_status_id,
        order_id
    ],function(error, results){
        if(error) throw error
        return res.send(results)
    })
})


app.get("/find/detail/:product_id",function(req, res){
    dbConn.query("SELECT detail FROM product WHERE id = ?",req.params.product_id, function(error, results){
        if(error) throw error
        return res.send(results[0])
    })
})
app.get("/find/amount/:product_id",function(req, res){
    dbConn.query("SELECT amount FROM product WHERE id = ?",req.params.product_id, function(error, results){
        if(error) throw error
        return res.send(results[0])
    })
})


// set port
app.listen(3000, function () {
    console.log("Unicode API is running.....");
})

module.exports = app