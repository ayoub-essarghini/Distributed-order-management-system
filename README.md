# 🚀 Distributed Order Management System

> ⚡ Event-driven microservices using Spring Boot 4.0, RabbitMQ, and RESTful APIs. Demonstrates async messaging, service decoupling, and scalable architecture patterns.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen) ![Java](https://img.shields.io/badge/Java-17-blue) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.0-orange) ![Docker](https://img.shields.io/badge/Docker-Ready-blue) ![License](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## 👋 Hey there!

So you want to learn microservices? You're in the right place! This isn't another tutorial that leaves you with half-working code. This is a **complete, working system** that you can run, break, fix, and learn from.

### What does it do?

Imagine you're running an online store. When someone orders a laptop:
1. **Order Service** takes the order
2. **Inventory Service** checks if you have it in stock
3. **Notification Service** sends confirmations

But here's the cool part - they don't talk directly to each other. They send messages through RabbitMQ (think of it as a super-smart post office). This means:
- If one service goes down, others keep working
- You can scale each service independently
- Adding new features is way easier

---

## ✨ What You'll Learn

- 🎯 How microservices actually communicate (spoiler: not with REST calls)
- 🔄 Asynchronous messaging (fire and forget, no waiting around)
- 📦 Docker (run the whole system with ONE command)
- 🐰 RabbitMQ (your message broker friend)
- 🌐 REST APIs (because we still need those)

---

## 🏃‍♂️ Quick Start (2 minutes)

**Prerequisites:**
- Docker installed (that's it!)

**Run it:**
```bash
git clone https://github.com/ayoub-essarghini/distributed-order-management-system.git
cd distributed-order-management-system
docker-compose up
```

**Test it:**
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"CUST-001","productId":"PROD-001","quantity":2,"price":999.99}'
```

Watch the magic happen in your terminal! 🎉

---

## 🏗️ How It Works

```
You → Order Service → RabbitMQ → Inventory Service
                          ↓
                   Notification Service
```

1. You create an order (via REST API)
2. Order Service saves it and publishes an event
3. RabbitMQ receives the event and distributes it
4. Inventory Service picks up the event and reserves stock
5. Notification Service picks up both events and logs them

**No service talks directly to another.** They're all independent. That's the beauty of it!

---

## 📦 What's Inside

### Order Service (Port 8081)
Takes your orders and publishes events. Simple as that.

**API Endpoints:**
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get specific order

### Inventory Service (Port 8082)
Listens for orders and manages stock. If you try to order 1000 laptops and there's only 50, it'll tell you.

**API Endpoints:**
- `GET /api/inventory` - See all products
- `GET /api/inventory/{id}` - Get specific product
- `POST /api/inventory/{id}/stock` - Add stock

### Notification Service (Port 8083)
The observer. It watches everything and logs notifications. In the real world, this would send emails or SMS.

**API Endpoints:**
- `GET /api/notifications` - See all notifications

### RabbitMQ (Ports 5672, 15672)
The message broker. The post office. The middleman. Whatever you call it, it's what makes this all work.

**Management UI:**
- http://localhost:15672 (guest/guest)

---

## 🎮 Try It Out

### Check what's in stock
```bash
curl http://localhost:8082/api/inventory
```

### Create an order
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-001",
    "productId": "PROD-001",
    "quantity": 2,
    "price": 999.99
  }'
```

### See the magic
```bash
# Check inventory again (stock decreased!)
curl http://localhost:8082/api/inventory

# Check notifications
curl http://localhost:8083/api/notifications
```

### Try to break it
```bash
# Order more than available
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-002",
    "productId": "PROD-001",
    "quantity": 1000,
    "price": 999.99
  }'

# Check notifications - you'll see an error message
curl http://localhost:8083/api/notifications
```

---

## 🛠️ Development

### Running locally (without Docker)

**1. Start RabbitMQ:**
```bash
docker-compose up rabbitmq -d
```

**2. Start services (separate terminals):**
```bash
cd order-service && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### Making changes

1. Edit your code
2. Rebuild: `docker-compose build <service-name>`
3. Restart: `docker-compose up -d <service-name>`

Or just restart everything:
```bash
docker-compose down && docker-compose up --build
```

---

## 📚 Understanding the Code

Each service has the same structure:

```
service/
├── model/          # Data classes (Order, Product, etc.)
├── controller/     # REST endpoints
├── service/        # Business logic
└── config/         # RabbitMQ setup
```

**Start here if you're new:**
1. Look at `OrderController.java` - see how REST APIs work
2. Check `OrderService.java` - see how messages are published
3. Open `InventoryService.java` - see how messages are consumed
4. Play with `RabbitMQConfig.java` - understand queue setup

---

## 🤔 Common Questions

**Q: Why not just call services directly with REST?**
A: Because if Inventory Service is down, Order Service would fail too. With messaging, orders are queued and processed when Inventory comes back up.

**Q: Is this production-ready?**
A: It's a great learning tool! For production, you'd add databases, authentication, error handling, monitoring, etc.

**Q: Can I add more services?**
A: Absolutely! That's the whole point. Add a Payment Service, Shipping Service, whatever you want!

**Q: What if I don't have Docker?**
A: Check the docs folder for local setup instructions.

---

## 🚀 What's Next?

Want to make this better? Here are some ideas:

- [ ] Add a database (PostgreSQL)
- [ ] Implement API Gateway
- [ ] Add authentication (JWT)
- [ ] Create a Payment Service
- [ ] Add unit tests

---

## 🐛 Troubleshooting

**Services won't start?**
- Make sure ports 8081, 8082, 8083, 5672, 15672 are free
- Check `docker-compose logs` for errors

**Can't connect to RabbitMQ?**
- Wait 30 seconds after starting (RabbitMQ takes time to initialize)
- Check http://localhost:15672 - if it loads, RabbitMQ is ready

**Code changes not reflected?**
- Run `docker-compose down && docker-compose up --build`

---

## 🤝 Contributing

Found a bug? Want to add a feature? PRs are welcome!

1. Fork it
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

No contribution is too small - even fixing typos helps!

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2025 Ayoub Essarghini
---

## ⭐ Show Some Love

If this helped you understand microservices, give it a star! It helps others find it too.

---

## 🙏 Acknowledgments

Built with:
- Lots of coffee ☕
- Spring Boot documentation
- Stack Overflow answers
- Trial and error (mostly error)

---

## 📞 Questions?

- Open an [issue](https://github.com/ayoub-essarghini/distributed-order-management-system/issues)
- Or just dive in and break things - that's how you learn!

---

<div align="center">

**Happy coding! 🎉**

Made with ❤️ for developers learning microservices

</div>