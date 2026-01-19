export interface Rental {
  id: number;
  carId: number;
  carBrand: string;
  carModel: string;
  userId: string;
  startDate: string;
  endDate: string;
  totalCost: number;
  status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
}