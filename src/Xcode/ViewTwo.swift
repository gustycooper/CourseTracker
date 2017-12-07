//
//  ViewTwo.swift
//  CourseTracker
//
//  Created by Diego  Bustamante on 10/18/17.
//  Copyright © 2017 Diego Bustamante. All rights reserved.
//

import Foundation
import UIKit


class ViewTwo: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var CourseTableView: UITableView!
    var courses: [Courses]? = []
    var myUUID = String()
    var myIndex = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("UUID YOL:" + myUUID)
        fetchCourses()
    }
    
    
    
    
    func fetchCourses() {
    // verify data
    let url = URL(string: "https://coursetrackerumw.herokuapp.com/courses?uid=" + myUUID)
    
    let task = URLSession.shared.dataTask(with: url!) {(data, response, error) in
    
    if error != nil{
        print("ERROR")
    }
    else{
        self.courses = [Courses]()
        if let content = data{
            do {
                let myJson = try JSONSerialization.jsonObject(with: content, options: .mutableContainers)
                if let coursesFromJson = myJson as? [[String: AnyObject]]{
                    for courseFromJson in coursesFromJson {
                        let course = Courses()
                        if let title = courseFromJson["desc"] as? String, let cid = courseFromJson["cid"] as? String{
                            course.title = title
                            course.cid = cid
                            print("cid: " + course.cid!)
                        }
                        self.courses?.append(course)
                        
                    }
                }
                DispatchQueue.main.sync {
                    self.CourseTableView.reloadData()
                }
            } catch let error { print(error)}
            }
        }
    }
    task.resume()
    
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "courseCell", for: indexPath) as! CourseCell
        cell.Title.text = self.courses?[indexPath.item].title
        cell.cid.text = self.courses?[indexPath.item].cid
        
        
        return cell
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let thirdController = segue.destination as! ViewThree
        thirdController.courseId = (self.courses?[myIndex].cid)!
        
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        myIndex = indexPath.row
        performSegue(withIdentifier: "cellAction", sender: self)
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int{
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return self.courses?.count ?? 0
    }
    
    
}
