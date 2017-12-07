//
//  ViewThree.swift
//  CourseTracker
//
//  Created by Diego  Bustamante on 10/28/17.
//  Copyright © 2017 Diego Bustamante. All rights reserved.
//
import Foundation
import UIKit

class ViewThree: UIViewController, UITableViewDelegate, UITableViewDataSource {

    var courseId = String()
    var assignments: [Assignments]? = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        fetchAssignments()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func fetchAssignments(){
        let url = URL(string: "https://coursetrackerumw.herokuapp.com/assignments?cid=" + courseId)
        let task = URLSession.shared.dataTask(with: url!){(data, response, error) in
            if error  != nil{
                print("ERROR")
            }
            else{
                self.assignments = [Assignments]()
                if let content = data{
                    do {
                        let myJson = try JSONSerialization.jsonObject(with: content, options: .mutableContainers)
                        if let assignmentsFromJson = myJson as? [[String: AnyObject]]{
                            for assignmentFromJson in assignmentsFromJson {
                                let assignment = Assignments()
                                if let due = assignmentFromJson["due"] as? String, let name = assignmentFromJson["name"] as? String{
                                    assignment.name = name
                                    assignment.due = due
                                    print("name: " + assignment.name!)
                                    print("due: " + assignment.due!)
                                }
                                self.assignments?.append(assignment)
                                
                            }
                        }
                        DispatchQueue.main.sync {
                            //self.CourseTableView.reloadData()
                        }
                    } catch let error { print(error)}
                }
            }
        }
        task.resume()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "assignmentCell", for: indexPath) as! AssignmentCell
        cell.name.text = self.assignments?[indexPath.item].name
        cell.due.text = self.assignments?[indexPath.item].due
        
        
        return cell
    }
    
    func numberOfSections(in tableView: UITableView) -> Int{
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return self.assignments?.count ?? 0
    }
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
