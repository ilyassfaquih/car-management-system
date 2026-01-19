# Car Management System - Frontend

Angular frontend application for the Car Management System.

## Features

- 🔐 User authentication with JWT tokens
- 🚗 Browse available cars
- 📋 View car details (brand, model, year, price)
- 💳 Rent cars with automatic availability updates
- 📊 View rental history
- ✅ Return rented cars
- 🎨 Modern, responsive UI with animations

## Prerequisites

- Node.js (v18 or higher)
- npm or yarn
- Angular CLI (`npm install -g @angular/cli`)

## Installation

```bash
cd frontend
npm install
```

## Configuration

Update the API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'  // Your API Gateway URL
};
```

## Running the Application

```bash
npm start
```

The application will be available at `http://localhost:4200`

## Building for Production

```bash
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── login/           # Login component
│   │   │   ├── cars-list/       # Car listing component
│   │   │   └── rentals/         # User rentals component
│   │   ├── services/
│   │   │   ├── auth.service.ts  # Authentication service
│   │   │   ├── car.service.ts   # Car management service
│   │   │   └── rental.service.ts # Rental service
│   │   ├── models/              # TypeScript interfaces
│   │   ├── guards/              # Route guards
│   │   ├── interceptors/        # HTTP interceptors
│   │   └── app.component.ts     # Root component
│   ├── environments/            # Environment configurations
│   └── styles.css               # Global styles
└── package.json
```

## Default Credentials (for testing)

- Username: `admin`
- Password: `admin123`

## API Endpoints Expected

The frontend expects the following API endpoints:

### Authentication
- `POST /api/auth/login` - User login

### Cars
- `GET /api/cars` - Get all cars
- `GET /api/cars/{id}` - Get car by ID
- `GET /api/cars/available` - Get available cars

### Rentals
- `POST /api/rentals` - Create a rental
- `GET /api/rentals/user` - Get user's rentals
- `GET /api/rentals/{id}` - Get rental by ID
- `PUT /api/rentals/{id}/return` - Return a car

## Features Breakdown

### Authentication
- JWT-based authentication
- Token stored in localStorage
- HTTP interceptor adds token to all requests
- Route guards protect authenticated routes

### Car Management
- Display all cars with details
- Filter available cars
- Real-time availability status
- Rent cars with date selection

### Rental Management
- View active and completed rentals
- Calculate rental duration and costs
- Return cars
- Rental status tracking

## Styling

- Modern gradient background
- Card-based design
- Responsive layout (mobile-friendly)
- Smooth animations and transitions
- Color-coded status indicators

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
