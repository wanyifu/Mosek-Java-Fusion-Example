package com.mosek.fusion.examples;
import mosek.fusion.*;
import java.io.PrintWriter;

public class mulan
{
    private static String[] weapon = { "Rocket", "Snake" };        
    private static double[] stock = { 150.0, 200.0 };
    
    private static double[][] supply_data = 
      { { 1.0, 2.0 },
        { 2.0, 2.0 }
        };
    
            
    private static double[] offense = { 3.0, 4.0 };
    public static void main(String[] args)
      throws SolutionError
    {
        Matrix supply = new DenseMatrix(supply_data);
        Model M = new Model("supply");      
        try
        {
      // "production" defines the amount of each product to bake.
          Variable production = M.variable("production", 
                                           new StringSet(weapon), 
                                           Domain.greaterThan(0.0));
        // The objective is to maximize the total revenue.
          M.objective("offense",
                      ObjectiveSense.Maximize, 
                      Expr.dot(offense, production));
        
        // The prodoction is constrained by stock:
          M.constraint(Expr.mul(supply, production), Domain.lessThan(stock));
          M.setLogHandler(new PrintWriter(System.out));
        
        // We solve and fetch the solution:
          M.solve();
          double[] res = production.level();
          System.out.println("Solution:");
          for (int i = 0; i < res.length; ++i)
          {
              System.out.println(" Number of " + weapon[i] + " : " + res[i]);
          }
          double total = 0;
          for (int i = 0; i < res.length; ++i) {
            total += res[i]*offense[i];
          }
          System.out.println(" Offense to Hun : $" + 
                         total);        
        }
        finally
        {
          M.dispose();
        }
    }
}