import { useQuery } from '@tanstack/react-query';

import * as homeService from '@/features/home/homeService';

export function useHome() {
  return useQuery({
    queryKey: ['me', 'resumo'],
    queryFn: homeService.getResumo,
  });
}
