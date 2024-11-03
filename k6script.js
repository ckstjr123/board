import http from 'k6/http';

export default function () {
  http.get('http://localhost:8080/free/13');
}

export const options = {
  scenarios: {
    boardDetail_scenario: {
      // name of the executor to use
      executor: 'per-vu-iterations', // 지정된 횟수만 반복

      // common scenario configuration
      startTime: '0s',
      gracefulStop: '5s',
      env: { EXAMPLEVAR: 'testing' },
      tags: { example_tag: 'testing' },

      // executor-specific configuration
      vus: 1000, // 가상 유저 수
      iterations: 1,
      maxDuration: '10s',
    },
  },
};